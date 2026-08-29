import { useMemo, useState } from "react";
import { Link } from "react-router-dom";
import {
    TicketPercent,
    Copy,
    Check,
    Calendar,
    Sparkles,
    Search,
    RefreshCw,
    ArrowRight
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useApplicablePromotions } from "./useApplicablePromotions";

const toDateOnly = (iso?: string | null) => (iso ? iso.slice(0, 10) : "—");

export default function CouponsPage() {
    const { items, loading, err, reload } = useApplicablePromotions();
    const [q, setQ] = useState("");
    const [copiedId, setCopiedId] = useState<string | null>(null);

    const filtered = useMemo(() => {
        const activeOnly = items.filter((p) => p.active === true);

        const qq = q.trim().toLowerCase();
        if (!qq) return activeOnly;

        return activeOnly.filter((p) => {
            const code = (p.code ?? "").toLowerCase();
            const title = (p.title ?? "").toLowerCase();
            const desc = (p.description ?? "").toLowerCase();
            return code.includes(qq) || title.includes(qq) || desc.includes(qq);
        });
    }, [items, q]);

    const handleCopy = (code?: string, id?: string) => {
        if (!code) return;
        navigator.clipboard.writeText(code);
        setCopiedId(id || code);
        setTimeout(() => setCopiedId(null), 2000);
    };

    return (
        <div className="w-full min-h-screen bg-zinc-950 text-white p-4 sm:p-6 lg:p-8">
            <div className="max-w-6xl mx-auto space-y-6">
                {/* Header */}
                <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4 border-b border-zinc-800/80 pb-6">
                    <div>
                        <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-orange-500/10 text-orange-400 text-xs font-black uppercase tracking-widest mb-1.5">
                            <TicketPercent className="w-3.5 h-3.5" /> Rewards & Discounts
                        </div>
                        <h1 className="text-3xl sm:text-4xl font-black tracking-tight text-white">
                            My Active Coupons
                        </h1>
                        <p className="text-sm text-zinc-400 mt-1">
                            Discounts earned from your Daily Spin and special promotional campaigns.
                        </p>
                    </div>

                    <Button
                        variant="outline"
                        size="sm"
                        className="rounded-xl border-zinc-800 bg-zinc-900 text-zinc-300 hover:text-white text-xs font-bold gap-1.5"
                        onClick={reload}
                    >
                        <RefreshCw className="w-3.5 h-3.5" /> Refresh
                    </Button>
                </div>

                {/* Search Bar */}
                <div className="relative max-w-md">
                    <Search className="w-4 h-4 text-zinc-400 absolute left-3.5 top-1/2 -translate-y-1/2 pointer-events-none" />
                    <Input
                        placeholder="Search coupons by code or title..."
                        value={q}
                        onChange={(e) => setQ(e.target.value)}
                        className="pl-10 bg-zinc-900/80 border-zinc-800 text-white placeholder:text-zinc-500 rounded-xl text-xs"
                    />
                </div>

                {err && (
                    <div className="rounded-2xl border border-red-500/30 bg-red-500/10 p-4 text-xs text-red-400">
                        {err}
                    </div>
                )}

                {/* Coupons Cards Grid */}
                {loading ? (
                    <div className="py-20 text-center space-y-3">
                        <div className="w-10 h-10 border-3 border-orange-500 border-t-transparent rounded-full animate-spin mx-auto" />
                        <p className="text-xs text-zinc-400">Loading your coupons...</p>
                    </div>
                ) : filtered.length === 0 ? (
                    <div className="py-20 text-center rounded-3xl border border-zinc-800 bg-zinc-900/30 p-8 max-w-md mx-auto space-y-3">
                        <TicketPercent className="w-12 h-12 text-zinc-600 mx-auto" />
                        <h3 className="text-lg font-bold text-white">No active coupons found</h3>
                        <p className="text-xs text-zinc-400">
                            Spin the Daily Wheel on the homepage to win discounts up to 50% OFF!
                        </p>
                        <Button asChild className="rounded-xl bg-orange-500 hover:bg-orange-600 text-white text-xs font-bold">
                            <Link to="/">Go to Daily Wheel</Link>
                        </Button>
                    </div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        {filtered.map((p) => (
                            <div
                                key={p.id}
                                className="rounded-2xl bg-zinc-900/70 border border-zinc-800/90 p-5 flex flex-col justify-between space-y-4 hover:border-orange-500/50 hover:shadow-lg transition-all duration-200 relative overflow-hidden"
                            >
                                <div className="absolute top-0 right-0 w-24 h-24 bg-orange-500/10 rounded-bl-full pointer-events-none" />

                                <div className="space-y-3">
                                    <div className="flex items-center justify-between">
                                        <span className="text-2xl font-black text-transparent bg-clip-text bg-gradient-to-r from-orange-400 to-amber-300 font-mono">
                                            {p.discount}% OFF
                                        </span>
                                        <span className="text-[10px] font-bold text-emerald-400 bg-emerald-500/10 border border-emerald-500/20 px-2 py-0.5 rounded-full">
                                            Active
                                        </span>
                                    </div>

                                    <div>
                                        <h4 className="text-sm font-bold text-white leading-snug">{p.title ?? "Daily Spin Promo"}</h4>
                                        <p className="text-xs text-zinc-400 mt-1">{p.description ?? "Valid on eligible hardware orders."}</p>
                                    </div>

                                    {/* Coupon Code Strip */}
                                    <div className="flex items-center justify-between gap-2 bg-zinc-950 border border-zinc-800 rounded-xl p-2">
                                        <span className="font-mono font-black text-xs text-orange-300 tracking-wider">
                                            {p.code}
                                        </span>
                                        <Button
                                            size="sm"
                                            variant="ghost"
                                            className="h-7 px-2.5 text-[11px] font-bold text-zinc-300 hover:text-white hover:bg-zinc-800 gap-1 rounded-lg"
                                            onClick={() => handleCopy(p.code, String(p.id))}
                                        >
                                            {copiedId === String(p.id) ? (
                                                <>
                                                    <Check className="w-3.5 h-3.5 text-emerald-400" /> Copied
                                                </>
                                            ) : (
                                                <>
                                                    <Copy className="w-3.5 h-3.5" /> Copy
                                                </>
                                            )}
                                        </Button>
                                    </div>
                                </div>

                                <div className="pt-3 border-t border-zinc-800/80 flex items-center justify-between text-[11px] text-zinc-500">
                                    <div className="flex items-center gap-1">
                                        <Calendar className="w-3 h-3" />
                                        <span>Expires: {toDateOnly(p.endDate)}</span>
                                    </div>

                                    <Link to="/build" className="text-orange-400 hover:text-orange-300 font-bold flex items-center gap-0.5">
                                        Use Now <ArrowRight className="w-3 h-3" />
                                    </Link>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}