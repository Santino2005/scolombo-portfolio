import { useAuth0 } from "@auth0/auth0-react";
import { useMemo, useState } from "react";
import { Link } from "react-router-dom";
import { ShoppingBag, Package, RefreshCw, Filter } from "lucide-react";
import { useMyPurchases } from "./MyPurchases.hooks";
import MyPurchaseCard from "@/components/common/purchases/card/MyPurchaseCard";
import { Button } from "@/components/ui/button";
import type { PurchaseStatus } from "@/types/PurchaseDto.ts";

export default function MyPurchasesPage() {
    const { getAccessTokenSilently } = useAuth0();
    const { purchases, loading } = useMyPurchases(getAccessTokenSilently);

    const STATUSES: PurchaseStatus[] = ["PAID", "READY", "DELIVERED", "CANCELLED"];
    const [filter, setFilter] = useState<PurchaseStatus | "ALL">("ALL");

    const filtered = useMemo(() => {
        const base = purchases ?? [];
        if (filter === "ALL") return base.filter((p) => p.status !== "CANCELLED");
        return base.filter((p) => p.status === filter);
    }, [purchases, filter]);

    if (loading) {
        return (
            <div className="min-h-screen bg-zinc-950 text-white flex items-center justify-center p-6">
                <div className="space-y-3 text-center">
                    <div className="w-10 h-10 border-3 border-orange-500 border-t-transparent rounded-full animate-spin mx-auto" />
                    <p className="text-sm text-zinc-400 font-semibold">Loading your purchases...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="w-full min-h-screen bg-zinc-950 text-white p-4 sm:p-6 lg:p-8">
            <div className="max-w-6xl mx-auto space-y-6">
                {/* Header */}
                <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4 border-b border-zinc-800/80 pb-6">
                    <div>
                        <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-orange-500/10 text-orange-400 text-xs font-black uppercase tracking-widest mb-1.5">
                            <Package className="w-3.5 h-3.5" /> Order History
                        </div>
                        <h1 className="text-3xl sm:text-4xl font-black tracking-tight text-white">
                            My Purchases & Orders
                        </h1>
                        <p className="text-sm text-zinc-400 mt-1">
                            Track status of custom rigs and individual hardware purchases.
                        </p>
                    </div>

                    <div className="flex items-center gap-3">
                        <select
                            value={filter}
                            onChange={(e) => setFilter(e.target.value as PurchaseStatus | "ALL")}
                            className="rounded-xl border border-zinc-800 bg-zinc-900 text-white text-xs font-semibold px-3 py-2 focus:outline-none"
                        >
                            <option value="ALL">All Active Orders</option>
                            {STATUSES.map((s) => (
                                <option key={s} value={s}>
                                    {s}
                                </option>
                            ))}
                        </select>
                    </div>
                </div>

                {filtered.length === 0 ? (
                    <div className="py-24 text-center rounded-3xl border border-zinc-800 bg-zinc-900/30 p-8 max-w-md mx-auto space-y-4">
                        <ShoppingBag className="w-12 h-12 text-zinc-600 mx-auto" />
                        <h3 className="text-lg font-bold text-white">No purchases found</h3>
                        <p className="text-xs text-zinc-400">
                            You have not placed any orders matching this filter yet.
                        </p>
                        <Button asChild className="rounded-xl bg-orange-500 hover:bg-orange-600 text-white text-xs font-bold">
                            <Link to="/market">Shop Hardware Market</Link>
                        </Button>
                    </div>
                ) : (
                    <div className="space-y-4">
                        {filtered.map((p) => (
                            <MyPurchaseCard key={p.id} purchase={p} />
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}
