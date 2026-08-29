import { useAuth0 } from "@auth0/auth0-react";
import { useEffect, useMemo, useState } from "react";
import { useLocation, useNavigate, useParams, Link } from "react-router-dom";
import {
    ShoppingCart,
    Check,
    Star,
    ShieldCheck,
    Zap,
    Truck,
    ArrowLeft,
    Share2,
    CheckCircle2,
    MessageSquarePlus
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import { addCartItem } from "@/services/Cart";
import { getProductBySkuPublic } from "@/services/Product";
import { createReview, getProductRating, getReviewsByProduct } from "@/services/Review";
import ReviewCarousel from "@/components/ReviewCarousel";
import type { Product } from "@/types/Entities";
import type { ReviewResponseDto, ProductRatingDto, CreateReviewDto } from "@/types/Review";

export default function ProductDetailPage() {
    const { skuPrefix } = useParams();
    const { isAuthenticated, loginWithRedirect, getAccessTokenSilently, user } = useAuth0();

    const [product, setProduct] = useState<Product | null>(null);
    const [reviews, setReviews] = useState<ReviewResponseDto[]>([]);
    const [rating, setRating] = useState<ProductRatingDto | null>(null);

    const [loading, setLoading] = useState(true);
    const [posting, setPosting] = useState(false);
    const [adding, setAdding] = useState(false);
    const [added, setAdded] = useState(false);
    const [quantity, setQuantity] = useState(1);

    const [stars, setStars] = useState<number>(5);
    const [message, setMessage] = useState("");
    const [reviewSuccess, setReviewSuccess] = useState(false);

    const productId = skuPrefix ?? "";

    const location = useLocation();
    const navigate = useNavigate();

    const returnTo = (location.state as any)?.returnTo as string | undefined;
    const builderState = (location.state as any)?.builderState as
        | { components: Record<string, string>; selectedComponentSku?: string }
        | undefined;

    async function load() {
        if (!skuPrefix) return;
        setLoading(true);
        try {
            const p = await getProductBySkuPublic(skuPrefix);
            setProduct(p);

            const [rv, rt] = await Promise.all([
                getReviewsByProduct(productId),
                getProductRating(productId),
            ]);

            setReviews(rv ?? []);
            setRating(rt ?? null);
        } catch (e) {
            console.error(e);
            setProduct(null);
            setReviews([]);
            setRating(null);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        load();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [skuPrefix]);

    const imageSrc = useMemo(() => {
        if (!product?.image) return "/placeholder.png";
        return `data:image/jpeg;base64,${product.image}`;
    }, [product?.image]);

    const inStock = (product?.stock ?? 0) > 0;
    const compName = product?.component?.displayName ?? product?.component?.skuPrefix ?? "Hardware";

    async function handleAddToCart() {
        if (!product) return;

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
                quantity: quantity,
                components: {},
            });
            setAdded(true);
            setTimeout(() => setAdded(false), 2000);
        } catch (e) {
            console.error(e);
            alert("Could not add to cart - check stock availability.");
        } finally {
            setAdding(false);
        }
    }

    async function handleCreateReview() {
        if (!isAuthenticated) {
            await loginWithRedirect({
                appState: { returnTo: window.location.pathname },
            });
            return;
        }
        if (!productId) return;

        const dto: CreateReviewDto = {
            skuPrefix: productId,
            qualification: Math.min(5, Math.max(1, stars)),
            message: message.trim(),
        };

        if (!dto.message) {
            alert("Please write a review message.");
            return;
        }

        setPosting(true);
        try {
            const token = await getAccessTokenSilently();
            await createReview(token, dto);
            setMessage("");
            setStars(5);
            setReviewSuccess(true);
            setTimeout(() => setReviewSuccess(false), 3000);
            await load();
        } catch (e) {
            console.error(e);
            alert("Could not post review.");
        } finally {
            setPosting(false);
        }
    }

    if (loading) {
        return (
            <div className="min-h-screen bg-zinc-950 text-white flex items-center justify-center p-6">
                <div className="space-y-3 text-center">
                    <div className="w-10 h-10 border-3 border-orange-500 border-t-transparent rounded-full animate-spin mx-auto" />
                    <p className="text-sm text-zinc-400 font-semibold">Loading product specs...</p>
                </div>
            </div>
        );
    }

    if (!product) {
        return (
            <div className="min-h-screen bg-zinc-950 text-white flex flex-col items-center justify-center p-6 space-y-4">
                <p className="text-xl font-bold text-zinc-300">Product not found</p>
                <Button asChild className="bg-orange-500 hover:bg-orange-600 rounded-xl">
                    <Link to="/market">Back to Market</Link>
                </Button>
            </div>
        );
    }

    return (
        <div className="w-full min-h-screen bg-zinc-950 text-white p-4 sm:p-6 lg:p-8">
            <div className="max-w-6xl mx-auto space-y-8">
                {/* Back Link */}
                <div className="flex items-center justify-between">
                    {returnTo ? (
                        <Button
                            variant="outline"
                            size="sm"
                            className="rounded-xl border-zinc-800 bg-zinc-900 text-zinc-300 hover:text-white text-xs font-bold gap-1.5"
                            onClick={() => {
                                if (builderState) {
                                    const key = returnTo.includes("/build/")
                                        ? `pc_draft_edit_${returnTo.split("/").pop()}`
                                        : "pc_draft_new";
                                    sessionStorage.setItem(
                                        key,
                                        JSON.stringify({
                                            selectedComponentSku: builderState.selectedComponentSku,
                                            components: builderState.components,
                                        })
                                    );
                                }
                                navigate(returnTo);
                            }}
                        >
                            <ArrowLeft className="w-4 h-4 text-orange-400" /> Back to PC Builder
                        </Button>
                    ) : (
                        <Button
                            asChild
                            variant="outline"
                            size="sm"
                            className="rounded-xl border-zinc-800 bg-zinc-900 text-zinc-300 hover:text-white text-xs font-bold gap-1.5"
                        >
                            <Link to="/market">
                                <ArrowLeft className="w-4 h-4 text-orange-400" /> Back to Market
                            </Link>
                        </Button>
                    )}

                    <Badge className="bg-orange-500/10 text-orange-400 border border-orange-500/30 text-xs font-black uppercase tracking-wider">
                        {compName}
                    </Badge>
                </div>

                {/* Main Product Hero Grid */}
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 items-start">
                    {/* Left: Product Image Showcase */}
                    <div className="space-y-4">
                        <div className="w-full aspect-square sm:h-96 rounded-3xl bg-gradient-to-br from-zinc-900 via-zinc-900/60 to-zinc-950 border border-zinc-800/80 p-8 flex items-center justify-center relative overflow-hidden shadow-2xl">
                            <img
                                src={imageSrc}
                                alt={product.name}
                                className="max-h-full max-w-full object-contain drop-shadow-[0_15px_30px_rgba(0,0,0,0.8)] hover:scale-105 transition-transform duration-300"
                            />

                            {inStock ? (
                                <div className="absolute top-4 left-4 bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-xs font-bold px-3 py-1 rounded-full flex items-center gap-1.5 backdrop-blur-md">
                                    <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse" />
                                    In Stock ({product.stock} units)
                                </div>
                            ) : (
                                <div className="absolute top-4 left-4 bg-red-500/10 border border-red-500/30 text-red-400 text-xs font-bold px-3 py-1 rounded-full backdrop-blur-md">
                                    Out of Stock
                                </div>
                            )}
                        </div>

                        {/* Trust Badges */}
                        <div className="grid grid-cols-3 gap-3">
                            <div className="p-3 rounded-2xl bg-zinc-900/60 border border-zinc-800/80 text-center flex flex-col items-center">
                                <ShieldCheck className="w-5 h-5 text-orange-400 mb-1" />
                                <span className="text-[11px] font-bold text-white">3-Year Warranty</span>
                                <span className="text-[10px] text-zinc-400">Direct RMA</span>
                            </div>
                            <div className="p-3 rounded-2xl bg-zinc-900/60 border border-zinc-800/80 text-center flex flex-col items-center">
                                <Zap className="w-5 h-5 text-amber-400 mb-1" />
                                <span className="text-[11px] font-bold text-white">Tested & Verified</span>
                                <span className="text-[10px] text-zinc-400">Stability Guaranteed</span>
                            </div>
                            <div className="p-3 rounded-2xl bg-zinc-900/60 border border-zinc-800/80 text-center flex flex-col items-center">
                                <Truck className="w-5 h-5 text-emerald-400 mb-1" />
                                <span className="text-[11px] font-bold text-white">Fast Shipping</span>
                                <span className="text-[10px] text-zinc-400">Insured Box</span>
                            </div>
                        </div>
                    </div>

                    {/* Right: Info, Price, Quantity & Add to Cart */}
                    <div className="space-y-6">
                        <div className="space-y-2">
                            <span className="text-xs font-mono text-zinc-400 tracking-wider">SKU: {product.skuPrefix}</span>
                            <h1 className="text-2xl sm:text-4xl font-black text-white tracking-tight leading-tight">
                                {product.name}
                            </h1>

                            {/* Rating */}
                            {rating && (
                                <div className="flex items-center gap-2 pt-1">
                                    <div className="flex text-amber-400">
                                        {Array.from({ length: 5 }).map((_, i) => (
                                            <Star
                                                key={i}
                                                className={`w-4 h-4 ${
                                                    i < Math.round(rating.avgRating)
                                                        ? "text-amber-400 fill-amber-400"
                                                        : "text-zinc-700"
                                                }`}
                                            />
                                        ))}
                                    </div>
                                    <span className="text-sm font-bold text-zinc-300">
                                        {rating.avgRating.toFixed(1)} / 5
                                    </span>
                                    <span className="text-xs text-zinc-400">
                                        ({rating.count} verified reviews)
                                    </span>
                                </div>
                            )}
                        </div>

                        {/* Price */}
                        <div className="p-4 rounded-2xl bg-zinc-900/80 border border-zinc-800 flex items-baseline justify-between">
                            <div>
                                <span className="text-xs font-bold text-zinc-400 uppercase tracking-wider block">Price</span>
                                <span className="text-4xl font-black text-transparent bg-clip-text bg-gradient-to-r from-orange-400 via-amber-300 to-orange-500">
                                    ${Number(product.price).toFixed(2)}
                                </span>
                            </div>
                            <span className="text-xs text-emerald-400 font-semibold bg-emerald-500/10 border border-emerald-500/20 px-2.5 py-1 rounded-full">
                                Free Standard Shipping
                            </span>
                        </div>

                        {/* Quantity and Add to Cart Action */}
                        <div className="space-y-3 pt-2">
                            <div className="flex items-center gap-3">
                                <div className="flex items-center bg-zinc-900 border border-zinc-800 rounded-xl p-1.5">
                                    <button
                                        type="button"
                                        className="w-9 h-9 rounded-lg hover:bg-zinc-800 flex items-center justify-center font-black text-zinc-300 text-sm"
                                        onClick={() => setQuantity((q) => Math.max(1, q - 1))}
                                        disabled={quantity <= 1}
                                    >
                                        -
                                    </button>
                                    <span className="w-10 text-center text-base font-bold text-white font-mono">
                                        {quantity}
                                    </span>
                                    <button
                                        type="button"
                                        className="w-9 h-9 rounded-lg hover:bg-zinc-800 flex items-center justify-center font-black text-zinc-300 text-sm"
                                        onClick={() => setQuantity((q) => Math.min(product.stock || 1, q + 1))}
                                        disabled={quantity >= (product.stock || 1)}
                                    >
                                        +
                                    </button>
                                </div>

                                <Button
                                    size="lg"
                                    className="flex-1 py-6 rounded-2xl bg-gradient-to-r from-orange-500 via-amber-500 to-orange-600 hover:from-orange-600 hover:to-orange-700 text-white font-black text-sm uppercase tracking-wide shadow-xl shadow-orange-500/25 border border-orange-400/30 active:scale-95 transition-all disabled:opacity-40"
                                    onClick={handleAddToCart}
                                    disabled={!inStock || adding}
                                >
                                    {added ? (
                                        <span className="flex items-center gap-2">
                                            <Check className="w-5 h-5" /> Added to Cart!
                                        </span>
                                    ) : adding ? (
                                        "Adding to Cart..."
                                    ) : (
                                        <span className="flex items-center gap-2">
                                            <ShoppingCart className="w-5 h-5" /> Add to Cart (${(Number(product.price) * quantity).toFixed(2)})
                                        </span>
                                    )}
                                </Button>
                            </div>
                        </div>

                        {/* Specs Table */}
                        {product.attributes && Object.keys(product.attributes).length > 0 && (
                            <div className="space-y-3 pt-4 border-t border-zinc-800">
                                <h3 className="text-xs uppercase font-extrabold tracking-wider text-orange-400">
                                    Technical Specifications
                                </h3>
                                <div className="grid grid-cols-2 gap-2">
                                    {Object.entries(product.attributes).map(([k, v]) => (
                                        <div
                                            key={k}
                                            className="bg-zinc-900/70 border border-zinc-800/80 rounded-xl p-2.5 text-xs flex flex-col"
                                        >
                                            <span className="text-zinc-400 font-medium capitalize">{k.replace("_", " ")}</span>
                                            <span className="text-white font-bold">{String(v)}</span>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        )}
                    </div>
                </div>

                {/* Reviews Section */}
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 pt-10 border-t border-zinc-800/80">
                    <div className="lg:col-span-2 space-y-4">
                        <div className="flex items-center justify-between">
                            <h2 className="text-xl font-black text-white">Community Reviews</h2>
                            <span className="text-xs text-zinc-400">{reviews.length} Verified Reviews</span>
                        </div>
                        <ReviewCarousel reviews={reviews} />
                    </div>

                    {/* Write Review Card */}
                    <div className="p-6 rounded-3xl bg-zinc-950/80 border border-zinc-800 space-y-4">
                        <div className="flex items-center gap-2 text-orange-400 font-bold text-sm">
                            <MessageSquarePlus className="w-4 h-4" /> Leave a Review
                        </div>

                        <div className="flex items-center justify-between">
                            <label className="text-xs text-zinc-400 font-semibold">Rating</label>
                            <select
                                value={stars}
                                onChange={(e) => setStars(Number(e.target.value))}
                                className="rounded-xl border border-zinc-800 bg-zinc-900 text-white px-3 py-1.5 text-xs focus:outline-none"
                            >
                                {[5, 4, 3, 2, 1].map((n) => (
                                    <option key={n} value={n}>
                                        {n} Stars {"★".repeat(n)}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <Input
                            placeholder="Share your benchmark results or experience..."
                            value={message}
                            onChange={(e) => setMessage(e.target.value)}
                            className="bg-zinc-900 border-zinc-800 text-white placeholder:text-zinc-500 rounded-xl text-xs"
                        />

                        <Button
                            className="w-full rounded-xl bg-orange-500 hover:bg-orange-600 text-white font-bold text-xs"
                            onClick={handleCreateReview}
                            disabled={posting}
                        >
                            {posting ? "Posting..." : "Submit Review"}
                        </Button>

                        {reviewSuccess && (
                            <div className="flex items-center gap-1.5 text-xs text-emerald-400 bg-emerald-500/10 p-2 rounded-lg">
                                <CheckCircle2 className="w-4 h-4" /> Review submitted successfully!
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}
