import { Cpu, Trash2, Edit3, ShoppingBag } from "lucide-react";
import type { Props } from "./CartItem.types";
import { Button } from "@/components/ui/button.tsx";

function CartItem({ item, updating, changeQuantityAbs, removeItem, navigate }: Props) {
    const isCustomPc = item.components && Object.keys(item.components).length > 0;
    const imageSrc = item.image ? `data:image/jpeg;base64,${item.image}` : "/placeholder.png";
    const isUpdating = updating[item.id];
    const stock = Number(item.stock ?? 0);

    const nextQty = item.quantity + 1;
    const availableStock = item.availableStock ?? item.stock ?? 99;
    const outOfStockForMore = nextQty > availableStock;

    return (
        <div className="flex flex-col justify-between p-4 sm:p-5 rounded-2xl bg-zinc-950/80 border border-zinc-800/80 hover:border-zinc-700 transition shadow-lg h-full space-y-4">
            <div className="flex gap-4 items-start">
                {/* Thumbnail */}
                <div className="h-20 w-20 sm:h-24 sm:w-24 rounded-2xl bg-zinc-900 border border-zinc-800 flex items-center justify-center p-2 shrink-0">
                    <img
                        src={imageSrc}
                        alt={item.productName ?? item.name ?? "Product"}
                        className="max-h-full max-w-full object-contain drop-shadow"
                    />
                </div>

                <div className="flex-1 min-w-0 space-y-1.5">
                    <div className="flex items-start justify-between gap-2">
                        <h3 className="text-sm sm:text-base font-extrabold text-white truncate leading-tight">
                            {item.name}
                        </h3>
                        <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0 rounded-lg text-zinc-500 hover:text-red-400 hover:bg-red-500/10 shrink-0"
                            onClick={() => removeItem(item.id)}
                            title="Remove from Cart"
                        >
                            <Trash2 className="w-4 h-4" />
                        </Button>
                    </div>

                    {isCustomPc ? (
                        <div className="space-y-2">
                            <div className="inline-flex items-center gap-1.5 px-2 py-0.5 rounded-md bg-orange-500/10 text-orange-400 border border-orange-500/20 text-[10px] font-black uppercase tracking-wider">
                                <Cpu className="w-3 h-3" /> Custom PC ({Object.keys(item.components).length} Parts)
                            </div>

                            <div>
                                <button
                                    onClick={() => navigate(`/build/${item.id}`)}
                                    className="inline-flex items-center gap-1 px-3 py-1.5 rounded-xl bg-zinc-900 border border-zinc-800 text-zinc-300 hover:text-white hover:border-orange-500/40 transition text-xs font-bold"
                                >
                                    <Edit3 className="w-3 h-3 text-orange-400" /> Modify Rig in Builder
                                </button>
                            </div>
                        </div>
                    ) : (
                        typeof item.stock !== "undefined" && (
                            <p className="text-[11px] text-zinc-400">
                                Stock: <span className="font-bold text-zinc-200">{stock}</span>
                            </p>
                        )
                    )}

                    {/* Quantity Stepper */}
                    <div className="flex items-center gap-3 pt-2">
                        <div className="flex items-center bg-zinc-900 border border-zinc-800 rounded-xl p-1 shrink-0">
                            <button
                                type="button"
                                className="w-7 h-7 rounded-lg hover:bg-zinc-800 flex items-center justify-center font-black text-zinc-300 text-xs disabled:opacity-30"
                                onClick={() => changeQuantityAbs(item.id, item.quantity - 1)}
                                disabled={isUpdating || item.quantity <= 1}
                            >
                                −
                            </button>
                            <span className="w-8 text-center text-xs font-bold text-white font-mono">
                                {item.quantity}
                            </span>
                            <button
                                type="button"
                                className="w-7 h-7 rounded-lg hover:bg-zinc-800 flex items-center justify-center font-black text-zinc-300 text-xs disabled:opacity-30"
                                onClick={() => changeQuantityAbs(item.id, item.quantity + 1)}
                                disabled={isUpdating || outOfStockForMore}
                            >
                                +
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            {/* Total Item Price */}
            <div className="pt-3 border-t border-zinc-900 flex justify-between items-baseline">
                <span className="font-semibold text-xs text-zinc-400">Item Total</span>
                <span className="font-black text-base sm:text-lg text-transparent bg-clip-text bg-gradient-to-r from-orange-400 to-amber-300">
                    ${Number(item.totalPrice).toFixed(2)}
                </span>
            </div>
        </div>
    );
}

export default CartItem;