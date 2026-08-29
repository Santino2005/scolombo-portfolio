import { useState } from "react";
import { Link } from "react-router-dom";
import { useAuth0 } from "@auth0/auth0-react";
import { ShoppingCart, Check, Star, ShieldCheck, Zap, X } from "lucide-react";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogClose } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { addCartItem } from "@/services/Cart";
import type { Product } from "@/types/Entities";

interface ProductQuickViewDialogProps {
    product: Product | null;
    open: boolean;
    onOpenChange: (open: boolean) => void;
    onAddedToCart?: () => void;
}

export default function ProductQuickViewDialog({
    product,
    open,
    onOpenChange,
    onAddedToCart,
}: ProductQuickViewDialogProps) {
    const { isAuthenticated, loginWithRedirect, getAccessTokenSilently } = useAuth0();
    const [quantity, setQuantity] = useState(1);
    const [adding, setAdding] = useState(false);
    const [added, setAdded] = useState(false);
    const [error, setError] = useState<string | null>(null);

    if (!product) return null;

    const imageSrc = product.image
        ? `data:image/jpeg;base64,${product.image}`
        : "/placeholder.png";

    const compPrefix = product.component?.skuPrefix ?? (product as any).componentSkuPrefix ?? "COMPONENT";
    const compName = product.component?.displayName ?? compPrefix;
    const inStock = (product.stock ?? 0) > 0;

    const handleAddToCart = async () => {
        if (!isAuthenticated) {
            await loginWithRedirect({
                appState: { returnTo: window.location.pathname },
            });
            return;
        }

        setAdding(true);
        setError(null);
        try {
            const token = await getAccessTokenSilently();
            await addCartItem(token, {
                productSku: product.skuPrefix,
                quantity: quantity,
                components: {},
            });
            setAdded(true);
            if (onAddedToCart) onAddedToCart();
            setTimeout(() => {
                setAdded(false);
                onOpenChange(false);
            }, 1200);
        } catch (e: any) {
            setError(e?.message ?? "Could not add product to cart.");
        } finally {
            setAdding(false);
        }
    };

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent className="sm:max-w-2xl bg-zinc-950 border border-zinc-800 text-white shadow-2xl p-0 overflow-hidden rounded-2xl max-h-[90vh] flex flex-col">
                <DialogHeader className="p-4 border-b border-zinc-800 flex flex-row items-center justify-between">
                    <div className="flex items-center gap-2">
                        <Badge className="bg-orange-500/20 text-orange-400 border border-orange-500/30 text-[11px] font-bold uppercase tracking-wider">
                            {compName}
                        </Badge>
                        <span className="text-xs text-zinc-400 font-mono">{product.skuPrefix}</span>
                    </div>
                    <DialogClose className="rounded-lg p-1.5 text-zinc-400 hover:text-white hover:bg-zinc-800 transition">
                        <X className="w-4 h-4" />
                    </DialogClose>
                </DialogHeader>

                <div className="p-6 overflow-y-auto flex-1 grid grid-cols-1 sm:grid-cols-2 gap-6 items-start">
                    {/* Left: Image & Badge */}
                    <div className="space-y-4">
                        <div className="w-full aspect-square rounded-2xl bg-gradient-to-br from-zinc-900 via-zinc-900/80 to-zinc-950 border border-zinc-800 p-6 flex items-center justify-center relative overflow-hidden shadow-inner group">
                            <img
                                src={imageSrc}
                                alt={product.name}
                                className="max-h-full max-w-full object-contain drop-shadow-[0_10px_20px_rgba(0,0,0,0.7)] group-hover:scale-105 transition-transform duration-300"
                                loading="lazy"
                            />
                            {inStock ? (
                                <div className="absolute top-3 left-3 bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-[10px] font-bold px-2 py-0.5 rounded-full flex items-center gap-1">
                                    <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse" />
                                    In Stock ({product.stock})
                                </div>
                            ) : (
                                <div className="absolute top-3 left-3 bg-red-500/10 border border-red-500/30 text-red-400 text-[10px] font-bold px-2 py-0.5 rounded-full">
                                    Out of Stock
                                </div>
                            )}
                        </div>

                        <div className="grid grid-cols-2 gap-2 text-xs text-zinc-400">
                            <div className="flex items-center gap-1.5 bg-zinc-900/60 p-2 rounded-xl border border-zinc-800/60">
                                <ShieldCheck className="w-4 h-4 text-orange-400 shrink-0" />
                                <span>3-Yr Warranty</span>
                            </div>
                            <div className="flex items-center gap-1.5 bg-zinc-900/60 p-2 rounded-xl border border-zinc-800/60">
                                <Zap className="w-4 h-4 text-amber-400 shrink-0" />
                                <span>Fast Delivery</span>
                            </div>
                        </div>
                    </div>

                    {/* Right: Info, Price, Attributes & Actions */}
                    <div className="flex flex-col h-full justify-between space-y-4">
                        <div>
                            <DialogTitle className="text-xl font-extrabold text-white tracking-tight leading-snug">
                                {product.name}
                            </DialogTitle>

                            <div className="mt-3 flex items-baseline gap-3">
                                <span className="text-3xl font-black text-transparent bg-clip-text bg-gradient-to-r from-orange-400 to-amber-300">
                                    ${Number(product.price).toFixed(2)}
                                </span>
                                <span className="text-xs text-zinc-400">USD + Taxes incl.</span>
                            </div>

                            {/* Attributes Table / Chips */}
                            {product.attributes && Object.keys(product.attributes).length > 0 && (
                                <div className="mt-4 space-y-2">
                                    <div className="text-xs uppercase font-bold tracking-wider text-zinc-400">
                                        Hardware Specifications
                                    </div>
                                    <div className="grid grid-cols-2 gap-1.5 max-h-36 overflow-y-auto pr-1">
                                        {Object.entries(product.attributes).map(([k, v]) => (
                                            <div
                                                key={k}
                                                className="bg-zinc-900/80 border border-zinc-800/80 rounded-lg px-2.5 py-1 text-[11px] flex flex-col"
                                            >
                                                <span className="text-zinc-400 capitalize font-medium">{k.replace("_", " ")}</span>
                                                <span className="text-zinc-200 font-bold truncate">{String(v)}</span>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            )}
                        </div>

                        {/* Quantity and Add to Cart Button */}
                        <div className="space-y-3 pt-2 border-t border-zinc-800">
                            {error && (
                                <div className="text-xs text-red-400 bg-red-500/10 border border-red-500/30 p-2 rounded-lg">
                                    {error}
                                </div>
                            )}

                            <div className="flex items-center gap-3">
                                <div className="flex items-center bg-zinc-900 border border-zinc-800 rounded-xl p-1 shrink-0">
                                    <button
                                        type="button"
                                        className="w-8 h-8 rounded-lg hover:bg-zinc-800 flex items-center justify-center font-bold text-zinc-300 disabled:opacity-30"
                                        onClick={() => setQuantity((q) => Math.max(1, q - 1))}
                                        disabled={quantity <= 1}
                                    >
                                        -
                                    </button>
                                    <span className="w-8 text-center text-sm font-bold text-white font-mono">
                                        {quantity}
                                    </span>
                                    <button
                                        type="button"
                                        className="w-8 h-8 rounded-lg hover:bg-zinc-800 flex items-center justify-center font-bold text-zinc-300 disabled:opacity-30"
                                        onClick={() => setQuantity((q) => Math.min(product.stock || 1, q + 1))}
                                        disabled={quantity >= (product.stock || 1)}
                                    >
                                        +
                                    </button>
                                </div>

                                <Button
                                    className="flex-1 py-5 rounded-xl bg-gradient-to-r from-orange-500 to-amber-500 hover:from-orange-600 hover:to-amber-600 text-white font-bold text-sm shadow-lg shadow-orange-500/20 active:scale-[0.98] transition-all disabled:opacity-40"
                                    disabled={!inStock || adding}
                                    onClick={handleAddToCart}
                                >
                                    {added ? (
                                        <span className="flex items-center gap-2">
                                            <Check className="w-4 h-4" /> Added to Cart!
                                        </span>
                                    ) : adding ? (
                                        <span>Adding...</span>
                                    ) : (
                                        <span className="flex items-center gap-2">
                                            <ShoppingCart className="w-4 h-4" /> Add to Cart
                                        </span>
                                    )}
                                </Button>
                            </div>

                            <div className="text-center">
                                <Link
                                    to={`/products/${product.skuPrefix}`}
                                    onClick={() => onOpenChange(false)}
                                    className="text-xs text-orange-400 hover:text-orange-300 underline underline-offset-2 transition"
                                >
                                    View Full Product Details & Benchmarks →
                                </Link>
                            </div>
                        </div>
                    </div>
                </div>
            </DialogContent>
        </Dialog>
    );
}
