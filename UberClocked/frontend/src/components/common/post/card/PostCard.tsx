import { Link } from "react-router-dom";
import { User, MessageSquare, Check, Tag, Eye } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import type { Props } from "./PostCard.types";

function PostCard({ post, imageUrl, isOwner, isBusy, isInterested, onInterested }: Props) {
    const disabled = isOwner || isBusy || isInterested || post.status !== "ACTIVE";
    const formattedPrice = Number(post.price ?? 0).toFixed(2);

    return (
        <div className="rounded-2xl bg-zinc-950/80 border border-zinc-800/80 hover:border-zinc-700 transition p-4 sm:p-5 shadow-lg space-y-4">
            <div className="flex flex-col sm:flex-row gap-4 items-start">
                {/* Thumbnail */}
                <div className="h-24 w-24 sm:h-28 sm:w-28 rounded-2xl bg-zinc-900 border border-zinc-800 flex items-center justify-center p-2 shrink-0 overflow-hidden">
                    {imageUrl ? (
                        <img
                            src={imageUrl}
                            alt={post.title}
                            className="max-h-full max-w-full object-contain drop-shadow"
                            loading="lazy"
                        />
                    ) : (
                        <span className="text-xs text-zinc-600 font-semibold">No Image</span>
                    )}
                </div>

                {/* Body Details */}
                <div className="flex-1 min-w-0 space-y-1.5">
                    <div className="flex flex-wrap items-center gap-2">
                        <Badge className="bg-orange-500/10 text-orange-400 border border-orange-500/20 text-[10px] font-black uppercase tracking-wider">
                            {post.category || "Hardware"}
                        </Badge>
                        <Badge
                            className={`text-[10px] font-bold ${
                                post.status === "ACTIVE"
                                    ? "bg-emerald-500/10 text-emerald-400 border-emerald-500/20"
                                    : "bg-zinc-800 text-zinc-400 border-zinc-700"
                            }`}
                        >
                            {post.status}
                        </Badge>
                    </div>

                    <Link to={`/posts/${post.id}`} className="block group">
                        <h3 className="text-base sm:text-lg font-black text-white group-hover:text-orange-400 transition leading-snug truncate">
                            {post.title}
                        </h3>
                    </Link>

                    <div className="flex items-center gap-2 text-xs text-zinc-400">
                        <span className="flex items-center gap-1 font-semibold text-zinc-300">
                            <User className="w-3.5 h-3.5 text-orange-400" />
                            {post.sellerUserName || "Community Member"}
                        </span>
                        <span>•</span>
                        <span className="font-bold text-orange-400">${formattedPrice}</span>
                    </div>

                    <p className="text-xs text-zinc-400 line-clamp-2 leading-relaxed pt-1">
                        {post.description}
                    </p>
                </div>
            </div>

            {/* Actions Bar */}
            <div className="pt-3 border-t border-zinc-900 flex items-center justify-between gap-2">
                <Button
                    asChild
                    variant="outline"
                    size="sm"
                    className="rounded-xl border-zinc-800 bg-zinc-900 text-zinc-300 hover:text-white text-xs font-bold gap-1.5"
                >
                    <Link to={`/posts/${post.id}`}>
                        <Eye className="w-3.5 h-3.5 text-orange-400" /> View Post & Specs
                    </Link>
                </Button>

                <Button
                    size="sm"
                    className={`rounded-xl text-xs font-bold px-4 transition-all ${
                        isInterested
                            ? "bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 cursor-default"
                            : "bg-orange-500 hover:bg-orange-600 text-white shadow-md shadow-orange-500/20"
                    }`}
                    onClick={() => onInterested(post)}
                    disabled={disabled}
                >
                    {isOwner ? (
                        "Your Post"
                    ) : isInterested ? (
                        <span className="flex items-center gap-1">
                            <Check className="w-3.5 h-3.5" /> Interested
                        </span>
                    ) : isBusy ? (
                        "Saving..."
                    ) : (
                        <span className="flex items-center gap-1">
                            <MessageSquare className="w-3.5 h-3.5" /> I'm Interested
                        </span>
                    )}
                </Button>
            </div>
        </div>
    );
}

export default PostCard;
