import { Plus, Check, Zap } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import type { Props } from "./ProductList.types";

function toImgSrc(image: any) {
    if (!image) return "/placeholder.png";
    if (typeof image === "string" && image.startsWith("data:")) return image;
    if (typeof image === "string") return `data:image/jpeg;base64,${image}`;
    return "/placeholder.png";
}

function ProductList({ products, onSelect }: Props) {
    if (products.length === 0) {
        return (
            <div className="py-16 text-center rounded-2xl border border-zinc-800 bg-zinc-950/60 p-8 text-zinc-400">
                <p className="text-sm font-semibold">Select a component category above to browse compatible parts.</p>
            </div>
        );
    }

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 max-h-[620px] overflow-y-auto pr-1">
            {products.map((p: any) => {
                const sku = p.skuPrefix ?? p.sku ?? p.id;
                const name = p.name ?? "Unnamed Component";
                const price = Number(p.price ?? 0);
                const stock = Number(p.stock ?? 0);
                const imgSrc = toImgSrc(p.image);
                const inStock = stock > 0;

                return (
                    <Card
                        key={sku}
                        className="bg-zinc-950/80 border border-zinc-800/80 hover:border-orange-500/50 rounded-2xl overflow-hidden transition-all duration-200 shadow-md group flex flex-col justify-between"
                    >
                        <div className="p-3 flex gap-3.5 items-center">
                            {/* Thumbnail */}
                            <div className="w-16 h-16 rounded-xl bg-zinc-900 border border-zinc-800 p-2 flex items-center justify-center shrink-0">
                                <img
                                    src={imgSrc}
                                    alt={name}
                                    className="max-h-full max-w-full object-contain drop-shadow group-hover:scale-105 transition-transform"
                                    loading="lazy"
                                />
                            </div>

                            {/* Details */}
                            <div className="min-w-0 flex-1 space-y-1">
                                <h4 className="text-xs sm:text-sm font-extrabold text-white line-clamp-2 leading-tight">
                                    {name}
                                </h4>
                                <div className="flex items-center gap-2">
                                    <span className="text-sm font-black text-transparent bg-clip-text bg-gradient-to-r from-orange-400 to-amber-300">
                                        ${price.toFixed(2)}
                                    </span>
                                    <span className="text-[10px] text-zinc-400 font-mono">
                                        {stock} in stock
                                    </span>
                                </div>
                            </div>
                        </div>

                        {/* Specs Highlights & Select Action */}
                        <div className="px-3 pb-3 pt-1 border-t border-zinc-900 flex items-center justify-between gap-2">
                            {p.attributes && Object.keys(p.attributes).length > 0 ? (
                                <div className="text-[10px] text-zinc-400 truncate max-w-[150px]">
                                    {Object.entries(p.attributes)
                                        .slice(0, 2)
                                        .map(([k, v]) => `${k}: ${v}`)
                                        .join(" • ")}
                                </div>
                            ) : (
                                <span className="text-[10px] text-zinc-500">Benchmark Ready</span>
                            )}

                            <Button
                                size="sm"
                                className="rounded-xl bg-orange-500 hover:bg-orange-600 text-white font-bold text-xs px-3 h-7 shadow-sm shadow-orange-500/20 active:scale-95 transition-all"
                                disabled={!inStock}
                                onClick={() => onSelect(p)}
                            >
                                <Plus className="w-3.5 h-3.5 mr-1" /> Add
                            </Button>
                        </div>
                    </Card>
                );
            })}
        </div>
    );
}

export default ProductList;