import { useNavigate } from "react-router-dom";
import { Trash2, Cpu, CheckCircle2, AlertTriangle, Zap, ShoppingBag } from "lucide-react";
import { Button } from "@/components/ui/button";
import type { Product } from "@/types/Entities.ts";
import { calculateTotalCost } from "./ProductSelectedPanel.utils";

export type SelectedItem = { key: string; product: Product };

export type Props = {
    selectedItems: SelectedItem[];
    onRemove?: (componentKey: string) => void;
    returnTo?: string;
    builderState?: {
        components: Record<string, string>;
        selectedComponentSku?: string;
        isEditMode?: boolean;
        itemId?: string;
    };
};

function getProductIdForRoute(p: any) {
    return p?.id ?? p?.skuPrefix ?? p?.sku ?? "";
}

function formatSlotKey(key: string) {
    if (key.startsWith("RAM_")) {
        return `RAM Slot ${key.split("_")[1]}`;
    }
    if (key.startsWith("SD_")) {
        return `Storage Slot ${key.split("_")[1]}`;
    }
    return key;
}

export default function ProductsSelectedPanel({
    selectedItems,
    onRemove,
    returnTo,
    builderState,
}: Props) {
    const navigate = useNavigate();
    const products = selectedItems.map((x) => x.product);
    const totalPrice = calculateTotalCost(products);

    const hasCase = selectedItems.some((it) => it.key === "CASE");

    // Rough wattage estimation based on TDP or base attributes
    const estimatedWattage = selectedItems.reduce((acc, it) => {
        const tdpStr = it.product.attributes?.tdp || "";
        const num = parseInt(tdpStr.replace(/\D/g, ""), 10);
        if (!isNaN(num) && num > 0) return acc + num;
        if (it.key === "CPU") return acc + 125;
        if (it.key === "GPU") return acc + 250;
        return acc + 15;
    }, 65); // 65W base motherboard/fans

    return (
        <div className="rounded-2xl bg-zinc-950/90 border border-zinc-800/80 p-5 shadow-xl flex flex-col justify-between space-y-4">
            <div className="flex items-center justify-between pb-3 border-b border-zinc-850">
                <div className="flex items-center gap-2">
                    <Cpu className="w-4 h-4 text-orange-400" />
                    <h3 className="text-base font-black text-white uppercase tracking-wider">
                        Current Rig Build
                    </h3>
                </div>
                <span className="text-xs font-bold text-orange-400 bg-orange-500/10 border border-orange-500/20 px-2 py-0.5 rounded-full">
                    {selectedItems.length} Parts
                </span>
            </div>

            {/* Warning if case is missing */}
            {!hasCase && (
                <div className="flex items-start gap-2 p-3 rounded-xl bg-amber-500/10 border border-amber-500/30 text-amber-300 text-xs">
                    <AlertTriangle className="w-4 h-4 shrink-0 mt-0.5" />
                    <span>
                        <strong className="text-white">Case (Chassis) required:</strong> Please select a Case to complete and order your custom PC.
                    </span>
                </div>
            )}

            {/* Selected Parts List */}
            <div className="overflow-y-auto max-h-72 space-y-2 pr-1">
                {selectedItems.length === 0 ? (
                    <div className="py-8 text-center text-zinc-500 text-xs font-semibold">
                        No components added yet. Click "+ Add" on parts from the catalog.
                    </div>
                ) : (
                    selectedItems.map(({ key, product }) => {
                        const routeId = getProductIdForRoute(product);
                        const slotLabel = formatSlotKey(key);

                        return (
                            <div
                                key={key}
                                className="flex items-center justify-between gap-3 rounded-xl bg-zinc-900/80 border border-zinc-800 p-2.5 hover:border-zinc-700 transition group"
                            >
                                <div
                                    className="min-w-0 flex-1 cursor-pointer"
                                    onClick={() =>
                                        navigate(`/products/${routeId}`, {
                                            state: { returnTo, builderState },
                                        })
                                    }
                                >
                                    <div className="flex items-center gap-1.5">
                                        <span className="text-[10px] font-black uppercase text-orange-400 bg-orange-500/10 px-1.5 py-0.2 rounded">
                                            {slotLabel}
                                        </span>
                                    </div>
                                    <p className="text-xs font-bold text-white truncate mt-0.5 group-hover:text-orange-400 transition">
                                        {product.name}
                                    </p>
                                    <p className="text-[11px] font-black text-transparent bg-clip-text bg-gradient-to-r from-orange-400 to-amber-300">
                                        ${Number(product.price ?? 0).toFixed(2)}
                                    </p>
                                </div>

                                {onRemove && (
                                    <Button
                                        variant="ghost"
                                        size="sm"
                                        className="h-8 w-8 p-0 rounded-lg text-zinc-400 hover:text-red-400 hover:bg-red-500/10 shrink-0"
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            onRemove(key);
                                        }}
                                        title="Remove from build"
                                    >
                                        <Trash2 className="w-3.5 h-3.5" />
                                    </Button>
                                )}
                            </div>
                        );
                    })
                )}
            </div>

            {/* Estimated Wattage & Cost Summary */}
            <div className="pt-3 border-t border-zinc-800/80 space-y-2">
                {selectedItems.length > 0 && (
                    <div className="flex items-center justify-between text-xs text-zinc-400 bg-zinc-900/50 p-2 rounded-xl border border-zinc-800/50">
                        <span className="flex items-center gap-1">
                            <Zap className="w-3.5 h-3.5 text-amber-400" /> Est. System Wattage:
                        </span>
                        <span className="font-bold text-white font-mono">~{estimatedWattage} Watts</span>
                    </div>
                )}

                <div className="flex items-baseline justify-between pt-1">
                    <span className="text-xs font-bold text-zinc-400 uppercase tracking-wider">Rig Subtotal</span>
                    <span className="text-2xl font-black text-transparent bg-clip-text bg-gradient-to-r from-orange-400 via-amber-300 to-orange-500">
                        ${totalPrice}
                    </span>
                </div>
            </div>
        </div>
    );
}