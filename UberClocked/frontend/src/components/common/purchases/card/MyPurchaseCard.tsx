import { Package, Clock, CheckCircle2, Truck, XCircle, ChevronRight } from "lucide-react";
import type { PurchaseResponseDto, PurchaseStatus } from "@/types/PurchaseDto";
import MyPurchaseItem from "../item/MyPurchasItem";

function getStatusBadge(status: PurchaseStatus) {
    switch (status) {
        case "PAID":
            return (
                <span className="inline-flex items-center gap-1 text-[11px] font-black text-amber-400 bg-amber-500/10 border border-amber-500/20 px-2.5 py-1 rounded-full">
                    <Clock className="w-3.5 h-3.5" /> Order Paid
                </span>
            );
        case "READY":
            return (
                <span className="inline-flex items-center gap-1 text-[11px] font-black text-blue-400 bg-blue-500/10 border border-blue-500/20 px-2.5 py-1 rounded-full">
                    <Package className="w-3.5 h-3.5" /> Assembled & Ready
                </span>
            );
        case "DELIVERED":
            return (
                <span className="inline-flex items-center gap-1 text-[11px] font-black text-emerald-400 bg-emerald-500/10 border border-emerald-500/20 px-2.5 py-1 rounded-full">
                    <CheckCircle2 className="w-3.5 h-3.5" /> Delivered
                </span>
            );
        case "CANCELLED":
            return (
                <span className="inline-flex items-center gap-1 text-[11px] font-black text-red-400 bg-red-500/10 border border-red-500/20 px-2.5 py-1 rounded-full">
                    <XCircle className="w-3.5 h-3.5" /> Cancelled
                </span>
            );
        default:
            return (
                <span className="text-[11px] font-bold text-zinc-400 bg-zinc-800 px-2 py-0.5 rounded-full">
                    {status}
                </span>
            );
    }
}

function MyPurchaseCard({ purchase }: { purchase: PurchaseResponseDto }) {
    return (
        <div className="p-5 sm:p-6 rounded-2xl bg-zinc-900/70 border border-zinc-800/90 shadow-xl space-y-4 hover:border-zinc-700 transition">
            <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 pb-4 border-b border-zinc-800/80">
                <div className="space-y-1">
                    <div className="flex items-center gap-2">
                        <span className="font-mono font-bold text-sm text-white">
                            Order #{purchase.id.slice(0, 8).toUpperCase()}
                        </span>
                        {getStatusBadge(purchase.status)}
                    </div>
                    <p className="text-xs text-zinc-400">
                        Placed on {new Date(purchase.createdAt).toLocaleDateString()} at{" "}
                        {new Date(purchase.createdAt).toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })}
                    </p>
                </div>

                <div className="sm:text-right">
                    <span className="text-[10px] text-zinc-500 uppercase font-bold tracking-wider block">Total Amount</span>
                    <span className="text-2xl font-black text-transparent bg-clip-text bg-gradient-to-r from-orange-400 via-amber-300 to-orange-500 font-mono">
                        ${Number(purchase.totalAmount).toFixed(2)}
                    </span>
                </div>
            </div>

            <div className="space-y-3">
                <div className="text-xs font-bold text-zinc-400 uppercase tracking-wider">
                    Purchased Hardware ({purchase.items?.length ?? 0} {purchase.items?.length === 1 ? "Item" : "Items"})
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                    {(purchase.items ?? []).map((it) => (
                        <MyPurchaseItem key={it.id} item={it} />
                    ))}
                </div>
            </div>
        </div>
    );
}

export default MyPurchaseCard;
