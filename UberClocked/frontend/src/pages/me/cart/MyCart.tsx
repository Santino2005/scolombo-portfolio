import { useAuth0 } from "@auth0/auth0-react";
import { useEffect, useMemo, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import {
    ShoppingCart,
    Tag,
    ArrowRight,
    Sparkles,
    CheckCircle2,
    ShieldCheck,
    Truck,
    CreditCard,
    Cpu,
    Trash2
} from "lucide-react";
import CartHeader from "@/components/common/cart/header/CartHeader";
import CartItem from "@/components/common/cart/item/CartItem";
import { Button } from "@/components/ui/button.tsx";
import { Input } from "@/components/ui/input.tsx";
import usePreference, { useCart } from "./MyCart.hooks";

function CartCouponPanel({
    open,
    setOpen,
    onApply,
    onRemove,
    appliedCode,
    discountAmount,
}: {
    open: boolean;
    setOpen: (v: boolean) => void;
    onApply: (code: string) => Promise<void>;
    onRemove: () => Promise<void>;
    appliedCode?: string | null;
    discountAmount?: number | null;
}) {
    const [code, setCode] = useState("");
    const [err, setErr] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    if (!open && !appliedCode) return null;

    return (
        <div className="mt-4 rounded-2xl bg-zinc-950/80 border border-zinc-800 p-4 shadow-lg">
            {appliedCode ? (
                <div className="flex flex-col sm:flex-row items-center justify-between gap-3 bg-zinc-900/80 p-3 rounded-xl border border-emerald-500/30">
                    <div className="flex items-center gap-2 text-xs font-semibold text-zinc-300">
                        <CheckCircle2 className="w-4 h-4 text-emerald-400 shrink-0" />
                        <span>
                            Active Coupon: <strong className="text-white font-mono">{appliedCode}</strong>
                        </span>
                        {typeof discountAmount === "number" && discountAmount > 0 && (
                            <span className="text-emerald-400 font-bold bg-emerald-500/10 px-2 py-0.5 rounded">
                                -${discountAmount.toFixed(2)} OFF
                            </span>
                        )}
                    </div>

                    <Button
                        variant="ghost"
                        size="sm"
                        className="rounded-xl text-red-400 hover:text-red-300 hover:bg-red-500/10 text-xs font-bold"
                        disabled={loading}
                        onClick={async () => {
                            setLoading(true);
                            setErr(null);
                            try {
                                await onRemove();
                                setOpen(false);
                                setCode("");
                            } catch {
                                setErr("Could not remove coupon.");
                            } finally {
                                setLoading(false);
                            }
                        }}
                    >
                        Remove Coupon
                    </Button>
                </div>
            ) : (
                <div className="space-y-2">
                    <div className="flex gap-2">
                        <Input
                            className="bg-zinc-900 border-zinc-800 text-white placeholder:text-zinc-500 rounded-xl text-xs flex-1"
                            placeholder="Enter coupon code (e.g. WHEEL-XXXXXX)"
                            value={code}
                            onChange={(e) => setCode(e.target.value)}
                        />

                        <Button
                            className="rounded-xl bg-orange-500 hover:bg-orange-600 text-white text-xs font-bold px-5"
                            disabled={loading || !code.trim()}
                            onClick={async () => {
                                setLoading(true);
                                setErr(null);
                                try {
                                    await onApply(code.trim());
                                    setCode("");
                                    setOpen(false);
                                } catch (e: any) {
                                    setErr(
                                        "This coupon cannot be redeemed or is not applicable to your cart."
                                    );
                                } finally {
                                    setLoading(false);
                                }
                            }}
                        >
                            {loading ? "Applying..." : "Apply Code"}
                        </Button>
                    </div>

                    {err && (
                        <div className="text-xs text-red-400 bg-red-500/10 border border-red-500/30 p-2 rounded-lg">
                            {err}
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}

export default function MyCartPage() {
    const { getAccessTokenSilently } = useAuth0();
    const {
        cart,
        updating,
        isLoading,
        loadCart,
        changeQuantityAbs,
        removeItem,
        applyCoupon,
        removeCoupon,
    } = useCart(getAccessTokenSilently);

    const navigate = useNavigate();
    const [visibleCount, setVisibleCount] = useState(10);
    const preferenceId = usePreference(getAccessTokenSilently, cart);
    const [couponOpen, setCouponOpen] = useState(false);

    useEffect(() => {
        loadCart();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const items = useMemo(() => cart?.items ?? [], [cart?.items]);
    const visibleItems = useMemo(() => items.slice(0, visibleCount), [items, visibleCount]);

    const subtotal = useMemo(() => {
        return items.reduce((acc, it: any) => acc + (Number(it.totalPrice) || 0), 0);
    }, [items]);

    const discount = useMemo(() => {
        const raw = cart?.discountAmount;
        return typeof raw === "number" ? raw : Number(raw ?? 0) || 0;
    }, [cart?.discountAmount]);

    const totalToPay = useMemo(() => Math.max(0, subtotal - discount), [subtotal, discount]);

    async function handleApplyCoupon(code: string) {
        await applyCoupon(code);
        await loadCart();
    }

    async function handleRemoveCoupon() {
        await removeCoupon();
        await loadCart();
    }

    if (isLoading) {
        return (
            <div className="min-h-screen bg-zinc-950 text-white flex items-center justify-center p-6">
                <div className="space-y-3 text-center">
                    <div className="w-10 h-10 border-3 border-orange-500 border-t-transparent rounded-full animate-spin mx-auto" />
                    <p className="text-sm text-zinc-400 font-semibold">Loading your cart...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="w-full min-h-screen bg-zinc-950 text-white p-4 sm:p-6 lg:p-8">
            <div className="max-w-6xl mx-auto space-y-8">
                {/* Header */}
                <div className="border-b border-zinc-800/80 pb-6 flex items-center justify-between">
                    <div>
                        <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-orange-500/10 text-orange-400 text-xs font-black uppercase tracking-widest mb-1.5">
                            <ShoppingCart className="w-3.5 h-3.5" /> Active Shopping Cart
                        </div>
                        <h1 className="text-3xl sm:text-4xl font-black tracking-tight text-white">
                            Your Order Summary
                        </h1>
                    </div>
                    <span className="text-xs font-bold text-zinc-400">
                        {items.length} {items.length === 1 ? "Item" : "Items"} in Cart
                    </span>
                </div>

                {items.length === 0 ? (
                    <div className="py-24 text-center rounded-3xl border border-zinc-800 bg-zinc-900/30 p-8 max-w-md mx-auto space-y-4">
                        <div className="w-16 h-16 rounded-full bg-zinc-900 border border-zinc-800 text-zinc-500 flex items-center justify-center mx-auto">
                            <ShoppingCart className="w-8 h-8" />
                        </div>
                        <h2 className="text-xl font-bold text-white">Your cart is empty</h2>
                        <p className="text-xs text-zinc-400">
                            Explore over 200+ hardware components or configure your custom dream rig.
                        </p>
                        <div className="flex justify-center gap-3 pt-2">
                            <Button asChild className="rounded-xl bg-orange-500 hover:bg-orange-600 text-white font-bold text-xs">
                                <Link to="/market">Shop Catalog</Link>
                            </Button>
                            <Button asChild variant="outline" className="rounded-xl border-zinc-700 bg-zinc-900 text-white font-bold text-xs">
                                <Link to="/build">PC Builder</Link>
                            </Button>
                        </div>
                    </div>
                ) : (
                    <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 items-start">
                        {/* Items Column (2 Cols on desktop) */}
                        <div className="lg:col-span-2 space-y-4">
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                {visibleItems.map((item: any) => (
                                    <CartItem
                                        key={item.id}
                                        item={item}
                                        updating={updating}
                                        changeQuantityAbs={changeQuantityAbs}
                                        removeItem={removeItem}
                                        navigate={navigate}
                                    />
                                ))}
                            </div>

                            {/* Coupon Activation Bar */}
                            <div className="p-4 rounded-2xl bg-zinc-900/60 border border-zinc-800/80 flex items-center justify-between gap-3">
                                <div className="flex items-center gap-2 text-xs font-bold text-zinc-300">
                                    <Tag className="w-4 h-4 text-orange-400" />
                                    <span>Have a Daily Roulette Coupon?</span>
                                </div>
                                <Button
                                    variant="outline"
                                    size="sm"
                                    className="rounded-xl border-zinc-700 bg-zinc-900 text-xs font-bold text-orange-400 hover:bg-zinc-800"
                                    onClick={() => setCouponOpen((v) => !v)}
                                >
                                    {cart.appliedPromotion?.code ? "Manage Coupon" : "Enter Code"}
                                </Button>
                            </div>

                            <CartCouponPanel
                                open={couponOpen}
                                setOpen={setCouponOpen}
                                onApply={handleApplyCoupon}
                                onRemove={handleRemoveCoupon}
                                appliedCode={cart.appliedPromotion?.code ?? null}
                                discountAmount={cart.discountAmount ?? null}
                            />
                        </div>

                        {/* Order Checkout Summary (1 Col sticky on desktop) */}
                        <div className="rounded-3xl bg-zinc-950/90 border border-zinc-800/90 p-6 space-y-6 shadow-2xl sticky top-24">
                            <h2 className="text-lg font-black text-white uppercase tracking-wider pb-3 border-b border-zinc-850">
                                Payment Details
                            </h2>

                            <div className="space-y-3 text-xs">
                                <div className="flex justify-between text-zinc-400">
                                    <span>Subtotal</span>
                                    <span className="font-bold text-white font-mono">${subtotal.toFixed(2)}</span>
                                </div>

                                {discount > 0 && (
                                    <div className="flex justify-between text-emerald-400 font-bold bg-emerald-500/10 p-2 rounded-xl border border-emerald-500/20">
                                        <span>Discount Applied</span>
                                        <span>-${discount.toFixed(2)}</span>
                                    </div>
                                )}

                                <div className="flex justify-between text-zinc-400">
                                    <span>Assembly & Testing Fee</span>
                                    <span className="font-bold text-zinc-200">Included ($50 base)</span>
                                </div>

                                <div className="flex justify-between text-zinc-400">
                                    <span>Insured Shipping</span>
                                    <span className="font-bold text-emerald-400">Free</span>
                                </div>

                                <div className="pt-3 border-t border-zinc-800 flex justify-between items-baseline">
                                    <span className="text-sm font-black text-white uppercase">Total to Pay</span>
                                    <span className="text-3xl font-black text-transparent bg-clip-text bg-gradient-to-r from-orange-400 via-amber-300 to-orange-500 font-mono">
                                        ${totalToPay.toFixed(2)}
                                    </span>
                                </div>
                            </div>

                            {/* Checkout CTA */}
                            <Button
                                asChild
                                size="lg"
                                className="w-full py-6 rounded-2xl bg-gradient-to-r from-orange-500 via-amber-500 to-orange-600 hover:from-orange-600 hover:to-orange-700 text-white font-black text-base uppercase tracking-wider shadow-xl shadow-orange-500/30 border border-orange-400/30 active:scale-95 transition-all"
                            >
                                <Link to={`/checkout/${preferenceId}`} className="flex items-center justify-center gap-2">
                                    <CreditCard className="w-5 h-5" /> Proceed to Checkout
                                </Link>
                            </Button>

                            <div className="grid grid-cols-2 gap-2 text-[10px] text-zinc-500 pt-2 border-t border-zinc-900">
                                <span className="flex items-center gap-1">
                                    <ShieldCheck className="w-3.5 h-3.5 text-emerald-400" /> Secure Checkout
                                </span>
                                <span className="flex items-center gap-1">
                                    <Truck className="w-3.5 h-3.5 text-orange-400" /> Express Delivery
                                </span>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}