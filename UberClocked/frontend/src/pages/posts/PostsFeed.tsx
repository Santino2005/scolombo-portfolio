import { useAuth0 } from "@auth0/auth0-react";
import { useEffect, useMemo, useState } from "react";
import { useLoaderData, Link } from "react-router-dom";
import { MessageSquare, Plus, Search, Sparkles, Filter, X } from "lucide-react";
import PostCard from "@/components/common/post/card/PostCard";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { fetchWithAuth } from "@/services/api";
import { markInterest } from "@/services/Market";
import type { PostResponseDto, UUID } from "@/types/Market";
import type { UserDataDto } from "@/types/UserDataDto";

function PostsFeed() {
    const { posts } = useLoaderData() as { posts: PostResponseDto[] };
    const { isAuthenticated, loginWithRedirect, getAccessTokenSilently } = useAuth0();
    const [q, setQ] = useState("");
    const [myUserId, setMyUserId] = useState<UUID | null>(null);
    const [busyId, setBusyId] = useState<UUID | null>(null);
    const [interestedIds, setInterestedIds] = useState<Set<UUID>>(new Set());
    const [selectedCategory, setSelectedCategory] = useState<string>("ALL");

    async function handleInterest(post: PostResponseDto) {
        if (!isAuthenticated) {
            await loginWithRedirect({
                authorizationParams: {
                    redirect_uri: window.location.origin + "/auth-callback",
                },
                appState: { returnTo: "/posts" },
            });
            return;
        }

        if (!myUserId || post.sellerId === myUserId) return;
        if (interestedIds.has(post.id)) return;

        setBusyId(post.id);
        try {
            const token = await getAccessTokenSilently();
            await markInterest(token, post.id);
            setInterestedIds((prev) => new Set(prev).add(post.id));
        } catch (e: any) {
            alert(e.message ?? "Could not mark interest");
        } finally {
            setBusyId(null);
        }
    }

    useEffect(() => {
        if (!isAuthenticated) {
            setMyUserId(null);
            return;
        }

        async function loadProfile() {
            try {
                const token = await getAccessTokenSilently();
                const data = await fetchWithAuth<UserDataDto>(
                    `${(import.meta.env.VITE_API_URL as string) || "http://localhost:8080"}/me`,
                    token
                );
                setMyUserId(data.id);
            } catch (err) {
                console.error(err);
            }
        }

        void loadProfile();
    }, [isAuthenticated, getAccessTokenSilently]);

    const categories = useMemo(() => {
        const set = new Set<string>();
        posts.forEach((p) => {
            if (p.category) set.add(p.category);
        });
        return Array.from(set);
    }, [posts]);

    const filtered = useMemo(() => {
        let result = posts;

        if (selectedCategory !== "ALL") {
            result = result.filter((p) => p.category === selectedCategory);
        }

        const s = q.trim().toLowerCase();
        if (s) {
            result = result.filter(
                (p) =>
                    `${p.title} ${p.category} ${p.description} ${p.sellerUserName}`
                        .toLowerCase()
                        .includes(s)
            );
        }

        return result;
    }, [posts, q, selectedCategory]);

    return (
        <div className="w-full min-h-screen bg-zinc-950 text-white p-4 sm:p-6 lg:p-8">
            <div className="max-w-5xl mx-auto space-y-6">
                {/* Header */}
                <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4 border-b border-zinc-800/80 pb-6">
                    <div>
                        <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-orange-500/10 text-orange-400 text-xs font-black uppercase tracking-widest mb-1.5">
                            <MessageSquare className="w-3.5 h-3.5" /> Community Exchange
                        </div>
                        <h1 className="text-3xl sm:text-4xl font-black tracking-tight text-white">
                            Community Hardware Board
                        </h1>
                        <p className="text-sm text-zinc-400 mt-1">
                            Discover second-hand bargains, custom modded rigs, and hardware trades.
                        </p>
                    </div>

                    <Button
                        asChild
                        className="rounded-2xl bg-gradient-to-r from-orange-500 to-amber-500 hover:from-orange-600 hover:to-amber-600 text-white font-extrabold text-xs px-5 shadow-lg shadow-orange-500/20"
                    >
                        <Link to="/posts/create" className="flex items-center gap-1.5">
                            <Plus className="w-4 h-4" /> Create New Post
                        </Link>
                    </Button>
                </div>

                {/* Filter & Search Bar */}
                <div className="flex flex-col sm:flex-row gap-3 items-center justify-between">
                    <div className="relative flex-1 w-full">
                        <Search className="w-4 h-4 text-zinc-400 absolute left-3.5 top-1/2 -translate-y-1/2 pointer-events-none" />
                        <Input
                            placeholder="Search exchange posts..."
                            value={q}
                            onChange={(e) => setQ(e.target.value)}
                            className="pl-10 bg-zinc-900/80 border-zinc-800 text-white placeholder:text-zinc-500 rounded-xl text-xs"
                        />
                        {q && (
                            <button
                                onClick={() => setQ("")}
                                className="absolute right-3 top-1/2 -translate-y-1/2 text-zinc-400 hover:text-white"
                            >
                                <X className="w-3.5 h-3.5" />
                            </button>
                        )}
                    </div>

                    {categories.length > 0 && (
                        <div className="flex items-center gap-1.5 overflow-x-auto max-w-full pb-1 sm:pb-0">
                            <button
                                type="button"
                                onClick={() => setSelectedCategory("ALL")}
                                className={`px-3 py-1.5 rounded-xl text-xs font-bold whitespace-nowrap transition ${
                                    selectedCategory === "ALL"
                                        ? "bg-orange-500 text-white"
                                        : "bg-zinc-900 border border-zinc-800 text-zinc-300 hover:text-white"
                                }`}
                            >
                                All ({posts.length})
                            </button>
                            {categories.map((cat) => (
                                <button
                                    key={cat}
                                    type="button"
                                    onClick={() => setSelectedCategory(cat)}
                                    className={`px-3 py-1.5 rounded-xl text-xs font-bold whitespace-nowrap transition ${
                                        selectedCategory === cat
                                            ? "bg-orange-500 text-white"
                                            : "bg-zinc-900 border border-zinc-800 text-zinc-300 hover:text-white"
                                    }`}
                                >
                                    {cat}
                                </button>
                            ))}
                        </div>
                    )}
                </div>

                {/* Posts List */}
                {filtered.length === 0 ? (
                    <div className="py-20 text-center rounded-3xl border border-zinc-800 bg-zinc-900/30 p-8 max-w-md mx-auto space-y-3">
                        <MessageSquare className="w-12 h-12 text-zinc-600 mx-auto" />
                        <h3 className="text-lg font-bold text-white">No exchange posts found</h3>
                        <p className="text-xs text-zinc-400">
                            Be the first to create a post or try searching for another hardware item.
                        </p>
                    </div>
                ) : (
                    <div className="grid grid-cols-1 gap-4">
                        {filtered.map((p) => {
                            const isOwner = !!myUserId && p.sellerId === myUserId;
                            const img = p.image ? `data:image/jpeg;base64,${p.image}` : "/placeholder.png";
                            return (
                                <PostCard
                                    key={p.id}
                                    post={p}
                                    imageUrl={img}
                                    isOwner={isOwner}
                                    isBusy={busyId === p.id}
                                    isInterested={interestedIds.has(p.id)}
                                    onInterested={handleInterest}
                                />
                            );
                        })}
                    </div>
                )}
            </div>
        </div>
    );
}

export default PostsFeed;
