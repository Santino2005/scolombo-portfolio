import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth0 } from "@auth0/auth0-react";
import { Eye, ShoppingCart, Check, Star, Zap } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { addCartItem } from "@/services/Cart";
import ProductQuickViewDialog from "@/components/common/products/ProductQuickViewDialog";
import type { Product } from "../types/Entities.ts";
import type { ReviewResponseDto, ProductRatingDto } from "@/types/Review";

interface ProductCardProps {
    product: Product;
    rating?: ProductRatingDto | null;
    firstReview?: ReviewResponseDto | null;
    variant?: "grid" | "featured" | "compact";
    onAddToCartSuccess?: () => void;
}

function Stars({ value }: { value: number }) {
    const full = Math.round(value);
    return (
        <span className="inline-flex items-center gap-0.5">
            {Array.from({ length: 5 }).map((_, i) => (
                <Star
                    key={i}
                    className={`w-3.5 h-3.5 ${
                        i < full ? "text-amber-400 fill-amber-400" : "text-zinc-600"
                    }`}
                />
            ))}
        </span>
    );
}

export function ProductCard({
    product,
    rating,
    firstReview,
    variant = "grid",
    onAddToCartSuccess,
}: ProductCardProps) {
    const navigate = useNavigate();
    const { isAuthenticated, loginWithRedirect, getAccessTokenSilently } = useAuth0();
    const [quickViewOpen, setQuickViewOpen] = useState(false);
    const [adding, setAdding] = useState(false);
    const [added, setAdded] = useState(false);

    const imageSrc = product.image
        ? `data:image/jpeg;base64,${product.image}`
        : "/placeholder.png";

    const compPrefix = product.component?.skuPrefix ?? (product as any).componentSkuPrefix ?? "";
    const compName = product.component?.displayName ?? compPrefix;
    const inStock = (product.stock ?? 0) > 0;

    const handleQuickAdd = async (e: React.MouseEvent) => {
        e.stopPropagation();
        e.preventDefault();

        if (!isAuthenticated) {
            await loginWithRedirect({
                appState: { returnTo: window.location.pathname },
            });
            return;
        }

        setAdding(true);
        try {
            const token = await getAccessTokenSilently();
            await addCartItem(token, {
                productSku: product.skuPrefix,
                quantity: 1,
                components: {},
            });
            setAdded(true);
            if (onAddToCartSuccess) onAddToCartSuccess();
            setTimeout(() => setAdded(false), 1500);
        } catch (err) {
            console.error(err);
        } finally {
            setAdding(false);
        }
    };

    // Horizontal / Featured Variant (e.g. for hero carousel)
    if (variant === "featured") {
        return (
            <>
                <Card className="w-full bg-zinc-950/90 border border-zinc-800/80 rounded-2xl overflow-hidden hover:border-orange-500/50 transition-all duration-300 shadow-xl group">
                    <CardContent className="p-4 sm:p-6 flex flex-col md:flex-row gap-6 items-center">
                        {/* Image Box */}
                        <div className="w-full md:w-1/2 h-64 sm:h-72 rounded-2xl bg-gradient-to-br from-zinc-900 via-zinc-900/60 to-zinc-950 border border-zinc-800 p-6 flex items-center justify-center relative overflow-hidden shrink-0">
                            <img
                                src={imageSrc}
                                alt={product.name}
                                className="max-h-full max-w-full object-contain drop-shadow-[0_10px_25px_rgba(0,0,0,0.8)] group-hover:scale-105 transition-transform duration-300"
                            />
                            {compName && (
                                <Badge className="absolute top-3 left-3 bg-orange-500/20 text-orange-400 border border-orange-500/30 text-[10px] font-black uppercase tracking-wider">
                                    {compName}
                                </Badge>
                            )}
                        </div>

                        {/* Info & Details */}
                        <div className="w-full md:w-1/2 flex flex-col justify-between space-y-4 min-w-0">
                            <div className="space-y-2">
                                <div className="flex items-center justify-between gap-2">
                                    <span className="text-xs font-mono text-zinc-400">{product.skuPrefix}</span>
                                    {inStock ? (
                                        <span className="inline-flex items-center gap-1 text-[11px] font-bold text-emerald-400 bg-emerald-500/10 px-2 py-0.5 rounded-full border border-emerald-500/20">
                                            <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse" />
                                            {product.stock} in stock
                                        </span>
                                    ) : (
                                        <span className="text-[11px] font-bold text-red-400 bg-red-500/10 px-2 py-0.5 rounded-full border border-red-500/20">
                                            Out of stock
                                        </span>
                                    )}
                                </div>

                                <h3 className="text-xl sm:text-2xl font-black text-white tracking-tight line-clamp-2">
                                    {product.name}
                                </h3>

                                <div className="flex items-baseline gap-2">
                                    <span className="text-2xl sm:text-3xl font-black text-transparent bg-clip-text bg-gradient-to-r from-orange-400 via-amber-300 to-orange-500">
                                        ${Number(product.price).toFixed(2)}
                                    </span>
                                    <span className="text-xs text-zinc-400">USD</span>
                                </div>

                                {rating && (
                                    <div className="flex items-center gap-2">
                                        <Stars value={rating.avgRating ?? 0} />
                                        <span className="text-xs font-bold text-zinc-300">
                                            {Number(rating.avgRating ?? 0).toFixed(1)}
                                        </span>
                                        <span className="text-xs text-zinc-500">
                                            ({rating.count ?? 0} reviews)
                                        </span>
                                    </div>
                                )}

                                {firstReview?.message && (
                                    <div className="rounded-xl border border-zinc-800 bg-zinc-900/60 p-3 text-xs text-zinc-300 line-clamp-2 italic">
                                        “{firstReview.message}”
                                    </div>
                                )}

                                {product.attributes && Object.keys(product.attributes).length > 0 && (
                                    <div className="flex flex-wrap gap-1.5 pt-1">
                                        {Object.entries(product.attributes).slice(0, 4).map(([k, v]) => (
                                            <Badge
                                                key={k}
                                                variant="secondary"
                                                className="bg-zinc-900 border border-zinc-800 text-zinc-300 text-[10px] py-0.5"
                                            >
                                                {k}: {String(v)}
                                            </Badge>
                                        ))}
                                    </div>
                                )}
                            </div>

                            {/* Actions */}
                            <div className="flex items-center gap-3 pt-2">
                                <Button
                                    variant="outline"
                                    className="border-zinc-700 bg-zinc-900 hover:bg-zinc-800 text-white rounded-xl text-xs font-bold gap-1.5"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        setQuickViewOpen(true);
                                    }}
                                >
                                    <Eye className="w-3.5 h-3.5 text-orange-400" /> Quick View
                                </Button>

                                <Button
                                    className="flex-1 bg-orange-500 hover:bg-orange-600 text-white font-bold rounded-xl text-xs gap-1.5 shadow-lg shadow-orange-500/20"
                                    disabled={!inStock || adding}
                                    onClick={handleQuickAdd}
                                >
                                    {added ? (
                                        <>
                                            <Check className="w-3.5 h-3.5" /> Added!
                                        </>
                                    ) : (
                                        <>
                                            <ShoppingCart className="w-3.5 h-3.5" /> Add to Cart
                                        </>
                                    )}
                                </Button>
                            </div>
                        </div>
                    </CardContent>
                </Card>

                <ProductQuickViewDialog
                    product={product}
                    open={quickViewOpen}
                    onOpenChange={setQuickViewOpen}
                />
            </>
        );
    }

    // Default Vertical Grid Card
    return (
        <>
            <Card
                className="w-full h-full bg-zinc-950/80 border border-zinc-800/80 rounded-2xl overflow-hidden hover:border-orange-500/50 hover:shadow-[0_8px_30px_rgba(249,115,22,0.12)] transition-all duration-300 flex flex-col justify-between group relative"
            >
                <div>
                    {/* Top Image Preview Container */}
                    <div className="relative w-full aspect-square bg-gradient-to-b from-zinc-900/90 to-zinc-950 p-5 flex items-center justify-center border-b border-zinc-800/80 overflow-hidden">
                        <img
                            src={imageSrc}
                            alt={product.name}
                            className="max-h-full max-w-full object-contain drop-shadow-[0_6px_16px_rgba(0,0,0,0.6)] group-hover:scale-108 transition-transform duration-300"
                            loading="lazy"
                        />

                        {/* Top Badges */}
                        <div className="absolute top-2.5 left-2.5 flex flex-col gap-1 items-start">
                            {compName && (
                                <span className="px-2 py-0.5 rounded-md bg-zinc-900/90 border border-zinc-700/80 text-[10px] font-black uppercase tracking-wider text-orange-400 backdrop-blur-sm shadow-sm">
                                    {compName}
                                </span>
                            )}
                        </div>

                        <div className="absolute top-2.5 right-2.5">
                            {inStock ? (
                                <span className="inline-flex items-center gap-1 text-[10px] font-bold text-emerald-400 bg-zinc-950/80 border border-emerald-500/30 px-2 py-0.5 rounded-full backdrop-blur-sm">
                                    <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse" />
                                    {product.stock} in stock
                                </span>
                            ) : (
                                <span className="text-[10px] font-bold text-red-400 bg-zinc-950/80 border border-red-500/30 px-2 py-0.5 rounded-full backdrop-blur-sm">
                                    Out of stock
                                </span>
                            )}
                        </div>

                        {/* Quick View Floating Button on Hover */}
                        <button
                            type="button"
                            onClick={(e) => {
                                e.stopPropagation();
                                setQuickViewOpen(true);
                            }}
                            className="absolute bottom-2.5 right-2.5 w-8 h-8 rounded-xl bg-zinc-900/90 border border-zinc-700 text-zinc-300 hover:text-white hover:bg-orange-500 hover:border-orange-500 flex items-center justify-center shadow-lg transition-all opacity-90 sm:opacity-0 sm:group-hover:opacity-100 sm:translate-y-1 sm:group-hover:translate-y-0"
                            title="Quick View Specs"
                        >
                            <Eye className="w-4 h-4" />
                        </button>
                    </div>

                    {/* Card Body */}
                    <CardContent className="p-4 space-y-2.5">
                        <Link to={`/products/${product.skuPrefix}`} className="block group-hover:text-orange-400 transition">
                            <h4 className="text-sm sm:text-base font-extrabold text-white line-clamp-2 leading-snug tracking-tight">
                                {product.name}
                            </h4>
                        </Link>

                        {/* Rating */}
                        {rating && rating.count > 0 ? (
                            <div className="flex items-center gap-1.5 text-xs">
                                <Stars value={rating.avgRating ?? 0} />
                                <span className="text-zinc-400 text-[11px]">
                                    ({rating.count})
                                </span>
                            </div>
                        ) : (
                            <div className="text-[11px] text-zinc-400 flex items-center gap-1">
                                <Zap className="w-3 h-3 text-orange-400" />
                                <span>Benchmark Verified</span>
                            </div>
                        )}

                        {/* Attributes Highlights */}
                        {product.attributes && Object.keys(product.attributes).length > 0 && (
                            <div className="flex flex-wrap gap-1 pt-0.5">
                                {Object.entries(product.attributes).slice(0, 2).map(([k, v]) => (
                                    <span
                                        key={k}
                                        className="text-[10px] font-medium bg-zinc-900 text-zinc-300 border border-zinc-800/80 px-1.5 py-0.5 rounded"
                                    >
                                        {String(v)}
                                    </span>
                                ))}
                            </div>
                        )}
                    </CardContent>
                </div>

                {/* Footer Price & Add Button */}
                <div className="p-4 pt-0 border-t border-zinc-900/60 mt-1 flex items-center justify-between gap-2">
                    <div>
                        <div className="text-[10px] text-zinc-400 uppercase font-semibold">Price</div>
                        <div className="text-lg font-black text-transparent bg-clip-text bg-gradient-to-r from-orange-400 to-amber-300">
                            ${Number(product.price).toFixed(2)}
                        </div>
                    </div>

                    <Button
                        size="sm"
                        className="rounded-xl bg-orange-500 hover:bg-orange-600 text-white font-bold text-xs px-3 shadow-md shadow-orange-500/20 active:scale-95 transition-all"
                        disabled={!inStock || adding}
                        onClick={handleQuickAdd}
                    >
                        {added ? (
                            <Check className="w-3.5 h-3.5" />
                        ) : adding ? (
                            <span className="animate-spin text-[10px]">...</span>
                        ) : (
                            <ShoppingCart className="w-3.5 h-3.5" />
                        )}
                    </Button>
                </div>
            </Card>

            <ProductQuickViewDialog
                product={product}
                open={quickViewOpen}
                onOpenChange={setQuickViewOpen}
            />
        </>
    );
}

export default ProductCard;