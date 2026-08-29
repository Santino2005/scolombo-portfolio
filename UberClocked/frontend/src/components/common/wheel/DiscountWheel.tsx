import { useEffect, useMemo, useRef, useState } from "react";
import { Copy, Check, Sparkles, Clock, AlertCircle, Gift, ArrowRight } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "@/components/ui/dialog";
import { getWheelPrizes, type WheelPrize, type WheelSpinResponse } from "@/services/wheelApi";
import { Link } from "react-router-dom";

export const DEFAULT_SEGMENTS = [
    { label: "5% OFF", discount: 5, color: "#18181b", textColor: "#f4f4f5", accentColor: "#f97316" },
    { label: "10% OFF", discount: 10, color: "#09090b", textColor: "#f4f4f5", accentColor: "#06b6d4" },
    { label: "15% OFF", discount: 15, color: "#18181b", textColor: "#f4f4f5", accentColor: "#a855f7" },
    { label: "20% OFF", discount: 20, color: "#09090b", textColor: "#f4f4f5", accentColor: "#10b981" },
    { label: "25% OFF", discount: 25, color: "#18181b", textColor: "#f4f4f5", accentColor: "#eab308" },
    { label: "50% OFF", discount: 50, color: "#ea580c", textColor: "#ffffff", accentColor: "#fbbf24" },
];

interface DiscountWheelProps {
    disabled?: boolean;
    canSpin?: boolean;
    nextSpinAt?: string | null;
    secondsRemaining?: number | null;
    onSpin: () => Promise<WheelSpinResponse>;
    onSpinComplete?: (prize: WheelPrize, code?: string) => void;
}

export default function DiscountWheel({
    disabled = false,
    canSpin = true,
    nextSpinAt,
    secondsRemaining: initialSecondsRemaining,
    onSpin,
    onSpinComplete,
}: DiscountWheelProps) {
    const wheelRef = useRef<HTMLDivElement | null>(null);
    const [prizes, setPrizes] = useState<typeof DEFAULT_SEGMENTS>(DEFAULT_SEGMENTS);
    const [spinning, setSpinning] = useState(false);
    const [wonPrize, setWonPrize] = useState<{ label: string; discount: number; code?: string } | null>(null);
    const [showPrizeModal, setShowPrizeModal] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [copied, setCopied] = useState(false);
    const [currentRotation, setCurrentRotation] = useState(0);

    // Timer countdown for already spun users
    const [countdown, setCountdown] = useState<number | null>(initialSecondsRemaining ?? null);

    useEffect(() => {
        if (initialSecondsRemaining !== undefined) {
            setCountdown(initialSecondsRemaining);
        }
    }, [initialSecondsRemaining]);

    useEffect(() => {
        if (countdown === null || countdown <= 0) return;
        const timer = setInterval(() => {
            setCountdown((prev) => (prev && prev > 1 ? prev - 1 : 0));
        }, 1000);
        return () => clearInterval(timer);
    }, [countdown]);

    // Load prizes from backend
    useEffect(() => {
        let mounted = true;
        (async () => {
            try {
                const fetched = await getWheelPrizes();
                if (mounted && fetched && fetched.length > 0) {
                    const colors = [
                        { color: "#18181b", textColor: "#f4f4f5", accentColor: "#f97316" },
                        { color: "#09090b", textColor: "#f4f4f5", accentColor: "#06b6d4" },
                        { color: "#18181b", textColor: "#f4f4f5", accentColor: "#a855f7" },
                        { color: "#09090b", textColor: "#f4f4f5", accentColor: "#10b981" },
                        { color: "#18181b", textColor: "#f4f4f5", accentColor: "#eab308" },
                        { color: "#ea580c", textColor: "#ffffff", accentColor: "#fbbf24" },
                    ];
                    const mapped = fetched.map((p, i) => {
                        const scheme = colors[i % colors.length];
                        return {
                            label: p.label,
                            discount: p.discount,
                            color: scheme.color,
                            textColor: scheme.textColor,
                            accentColor: scheme.accentColor,
                        };
                    });
                    setPrizes(mapped);
                }
            } catch {
                // Keep default
            }
        })();
        return () => {
            mounted = false;
        };
    }, []);

    const numSegments = prizes.length;
    const sliceAngle = 360 / numSegments;

    const handleSpin = async () => {
        if (disabled || !canSpin || spinning || (countdown !== null && countdown > 0)) return;

        setError(null);
        setSpinning(true);

        try {
            const res = await onSpin();

            if (!res.canSpin || !res.prize) {
                throw new Error("You already spun the wheel today or limit reached.");
            }

            const wonLabel = res.prize.label;
            const wonDiscount = res.prize.discount;
            const promoCode = res.promotion?.code;

            // Find matching index in segments
            let targetIndex = prizes.findIndex(
                (p) => p.label.toLowerCase() === wonLabel.toLowerCase() || p.discount === wonDiscount
            );
            if (targetIndex === -1) targetIndex = 0;

            // Exact calculation: center of segment targetIndex relative to 12 o'clock (0 deg)
            const segmentCenterAngle = (targetIndex + 0.5) * sliceAngle;
            // Angle to align segment center with 12 o'clock needle after clockwise rotation
            const alignAngle = (360 - (segmentCenterAngle % 360)) % 360;

            const extraSpins = 6;
            const baseRot = Math.ceil(currentRotation / 360) * 360;
            const targetRotation = baseRot + extraSpins * 360 + alignAngle;

            setCurrentRotation(targetRotation);

            const el = wheelRef.current;
            if (el) {
                el.style.transition = "transform 3.8s cubic-bezier(0.12, 0.95, 0.15, 1)";
                el.style.transform = `rotate(${targetRotation}deg)`;
            }

            // Wait for spin animation to finish
            await new Promise((r) => setTimeout(r, 4000));

            const prizeResult = {
                label: wonLabel,
                discount: wonDiscount,
                code: promoCode,
            };
            setWonPrize(prizeResult);
            setShowPrizeModal(true);

            if (onSpinComplete) {
                onSpinComplete(res.prize, promoCode);
            }
        } catch (e: any) {
            setError(e?.message ?? "Error spinning wheel");
        } finally {
            setSpinning(false);
        }
    };

    const copyCouponCode = () => {
        if (!wonPrize?.code) return;
        navigator.clipboard.writeText(wonPrize.code);
        setCopied(true);
        setTimeout(() => setCopied(false), 2500);
    };

    const formatCountdown = (secs: number) => {
        const h = Math.floor(secs / 3600);
        const m = Math.floor((secs % 3600) / 60);
        const s = secs % 60;
        return `${h}h ${m}m ${s}s`;
    };

    const isLocked = !canSpin || (countdown !== null && countdown > 0);

    return (
        <div className="w-full max-w-md mx-auto flex flex-col items-center select-none">
            {/* Outer Roulette Container with Glowing Ring */}
            <div className="relative w-72 h-72 sm:w-88 sm:h-88 my-4 flex items-center justify-center">
                {/* Neon Glow backdrop */}
                <div className="absolute inset-0 rounded-full bg-gradient-to-tr from-orange-500/30 via-amber-500/20 to-purple-600/20 blur-2xl animate-pulse" />

                {/* Outer Bezel Ring */}
                <div className="absolute inset-0 rounded-full bg-gradient-to-b from-zinc-800 via-zinc-950 to-black p-3 shadow-[0_20px_50px_rgba(0,0,0,0.8),0_0_30px_rgba(249,115,22,0.25)] border-2 border-zinc-700/80 flex items-center justify-center">
                    {/* Top Pointer Needle (Fixed at 12 o'clock) */}
                    <div className="absolute -top-3.5 left-1/2 -translate-x-1/2 z-30 flex flex-col items-center drop-shadow-[0_4px_12px_rgba(0,0,0,0.9)]">
                        <div className="w-0 h-0 border-l-[14px] border-r-[14px] border-t-[26px] border-l-transparent border-r-transparent border-t-orange-500" />
                        <div className="w-3.5 h-3.5 -mt-7 rounded-full bg-white shadow-md border-2 border-orange-600 ring-2 ring-orange-400/50" />
                    </div>

                    {/* Rotating Wheel Element */}
                    <div
                        ref={wheelRef}
                        className="relative w-full h-full rounded-full overflow-hidden shadow-inner border border-zinc-800"
                        style={{ transformOrigin: "50% 50%" }}
                    >
                        <svg viewBox="0 0 100 100" className="w-full h-full block">
                            {/* SVG Slices */}
                            {prizes.map((p, index) => {
                                const startAngle = index * sliceAngle;
                                const endAngle = (index + 1) * sliceAngle;

                                // Angle clockwise from top (12 o'clock)
                                const startRad = ((startAngle - 90) * Math.PI) / 180;
                                const endRad = ((endAngle - 90) * Math.PI) / 180;

                                const x1 = 50 + 50 * Math.cos(startRad);
                                const y1 = 50 + 50 * Math.sin(startRad);
                                const x2 = 50 + 50 * Math.cos(endRad);
                                const y2 = 50 + 50 * Math.sin(endRad);

                                const pathData = `M 50 50 L ${x1} ${y1} A 50 50 0 0 1 ${x2} ${y2} Z`;

                                return (
                                    <path
                                        key={`slice-${index}`}
                                        d={pathData}
                                        fill={p.color}
                                        stroke="#27272a"
                                        strokeWidth="0.75"
                                    />
                                );
                            })}

                            {/* Spoke Dividers with Accent Lines */}
                            {prizes.map((p, index) => {
                                const angle = index * sliceAngle;
                                const rad = ((angle - 90) * Math.PI) / 180;
                                const x = 50 + 50 * Math.cos(rad);
                                const y = 50 + 50 * Math.sin(rad);

                                return (
                                    <line
                                        key={`spoke-${index}`}
                                        x1="50"
                                        y1="50"
                                        x2={x}
                                        y2={y}
                                        stroke="#3f3f46"
                                        strokeWidth="0.8"
                                    />
                                );
                            })}

                            {/* Text Labels inside each segment */}
                            {prizes.map((p, index) => {
                                const midAngle = (index + 0.5) * sliceAngle;

                                return (
                                    <g
                                        key={`text-group-${index}`}
                                        transform={`rotate(${midAngle}, 50, 50)`}
                                    >
                                        {/* Label text placed along radial line */}
                                        <text
                                            x="50"
                                            y="17"
                                            textAnchor="middle"
                                            dominantBaseline="middle"
                                            fill={p.textColor}
                                            fontSize="4.8"
                                            fontWeight="900"
                                            letterSpacing="0.05em"
                                            style={{
                                                textShadow: `0 0 6px ${p.accentColor}90`,
                                                fontFamily: "system-ui, -apple-system, sans-serif",
                                            }}
                                        >
                                            {p.label}
                                        </text>

                                        {/* Accent Dot */}
                                        <circle
                                            cx="50"
                                            cy="25"
                                            r="1.2"
                                            fill={p.accentColor}
                                        />
                                    </g>
                                );
                            })}

                            {/* Outer Rim Ring */}
                            <circle
                                cx="50"
                                cy="50"
                                r="49"
                                fill="none"
                                stroke="#f97316"
                                strokeWidth="0.75"
                                strokeOpacity="0.4"
                            />
                        </svg>

                        {/* Subtle Radial Gradient Overlay */}
                        <div
                            className="absolute inset-0 rounded-full pointer-events-none opacity-25"
                            style={{
                                background: "radial-gradient(circle at 35% 25%, rgba(255,255,255,0.6) 0%, transparent 60%)",
                            }}
                        />
                    </div>

                    {/* Center Hub with Spin Button Badge */}
                    <div className="absolute inset-0 m-auto w-16 h-16 sm:w-20 sm:h-20 rounded-full bg-gradient-to-b from-zinc-800 via-zinc-900 to-zinc-950 border-3 border-orange-500 shadow-[0_6px_20px_rgba(0,0,0,0.9)] flex flex-col items-center justify-center z-20 pointer-events-none">
                        <Sparkles className="w-5 h-5 text-orange-400 animate-spin" style={{ animationDuration: "12s" }} />
                        <span className="text-[9px] font-black uppercase tracking-widest text-orange-400 mt-0.5">
                            ROLL
                        </span>
                    </div>

                    {/* Lock overlay if on cooldown */}
                    {isLocked && (
                        <div className="absolute inset-0 rounded-full bg-zinc-950/80 backdrop-blur-xs z-25 flex flex-col items-center justify-center p-4 text-center">
                            <Clock className="w-8 h-8 text-orange-400 mb-1 animate-pulse" />
                            <span className="text-xs font-bold text-white uppercase tracking-wider">Next Spin In</span>
                            <span className="text-sm font-black text-orange-400 font-mono">
                                {countdown !== null && countdown > 0 ? formatCountdown(countdown) : "Tomorrow"}
                            </span>
                        </div>
                    )}
                </div>
            </div>

            {/* Controls / Spin Button */}
            <div className="w-full px-2 mt-4 space-y-3">
                <Button
                    size="lg"
                    className="w-full py-6 text-base font-extrabold tracking-wide uppercase rounded-2xl bg-gradient-to-r from-orange-500 via-amber-500 to-orange-600 hover:from-orange-600 hover:to-orange-700 text-white shadow-xl shadow-orange-500/25 border border-orange-400/30 active:scale-[0.98] transition-all disabled:opacity-50 disabled:cursor-not-allowed disabled:shadow-none"
                    onClick={handleSpin}
                    disabled={disabled || isLocked || spinning}
                >
                    {spinning ? (
                        <span className="flex items-center gap-2">
                            <Sparkles className="w-5 h-5 animate-spin" /> Spinning Lucky Wheel...
                        </span>
                    ) : isLocked ? (
                        <span className="flex items-center gap-2">
                            <Clock className="w-5 h-5" /> Already Claimed Today
                        </span>
                    ) : (
                        <span className="flex items-center gap-2">
                            <Gift className="w-5 h-5" /> Spin Daily Discount
                        </span>
                    )}
                </Button>

                {error && (
                    <div className="flex items-center gap-2 p-3 rounded-xl border border-red-500/40 bg-red-500/10 text-red-400 text-xs">
                        <AlertCircle className="w-4 h-4 shrink-0" />
                        <span>{error}</span>
                    </div>
                )}
            </div>

            {/* Winner Announcement Modal Popup */}
            <Dialog open={showPrizeModal} onOpenChange={setShowPrizeModal}>
                <DialogContent className="sm:max-w-md bg-zinc-950 border border-orange-500/40 text-white shadow-2xl p-6 rounded-3xl">
                    <DialogHeader className="text-center space-y-2">
                        <div className="mx-auto w-14 h-14 rounded-full bg-gradient-to-br from-orange-500 to-amber-500 flex items-center justify-center shadow-lg shadow-orange-500/30">
                            <Gift className="w-8 h-8 text-white" />
                        </div>
                        <DialogTitle className="text-2xl font-black tracking-tight text-white">
                            Congratulations! 🎉
                        </DialogTitle>
                        <DialogDescription className="text-zinc-400 text-sm">
                            You just unlocked an exclusive daily discount!
                        </DialogDescription>
                    </DialogHeader>

                    {wonPrize && (
                        <div className="space-y-4 my-2">
                            <div className="text-center py-4 px-6 rounded-2xl bg-zinc-900/90 border border-zinc-800 shadow-inner">
                                <div className="text-xs uppercase tracking-widest font-bold text-orange-400 mb-1">
                                    Your Prize
                                </div>
                                <div className="text-4xl font-black tracking-tight text-transparent bg-clip-text bg-gradient-to-r from-orange-400 via-amber-300 to-orange-500">
                                    {wonPrize.label}
                                </div>
                                <div className="text-xs text-zinc-400 mt-1">
                                    {wonPrize.discount}% OFF applied automatically to eligible items
                                </div>
                            </div>

                            {wonPrize.code && (
                                <div className="space-y-1.5">
                                    <div className="text-xs font-semibold text-zinc-400 flex items-center justify-between">
                                        <span>Coupon Code:</span>
                                        <span className="text-[11px] text-orange-400">Valid for 24 hours</span>
                                    </div>
                                    <div className="flex items-center gap-2 bg-zinc-900 border border-orange-500/30 rounded-xl p-2">
                                        <span className="flex-1 font-mono font-bold text-base text-center text-orange-300 tracking-wider">
                                            {wonPrize.code}
                                        </span>
                                        <Button
                                            size="sm"
                                            variant="secondary"
                                            className="bg-orange-500 hover:bg-orange-600 text-white rounded-lg px-3 py-1.5 h-auto text-xs font-semibold flex items-center gap-1.5 transition-all"
                                            onClick={copyCouponCode}
                                        >
                                            {copied ? (
                                                <>
                                                    <Check className="w-3.5 h-3.5" /> Copied!
                                                </>
                                            ) : (
                                                <>
                                                    <Copy className="w-3.5 h-3.5" /> Copy
                                                </>
                                            )}
                                        </Button>
                                    </div>
                                </div>
                            )}

                            <div className="grid grid-cols-2 gap-3 pt-2">
                                <Button
                                    asChild
                                    variant="outline"
                                    className="rounded-xl border-zinc-700 bg-zinc-900 hover:bg-zinc-800 text-white text-xs font-bold"
                                    onClick={() => setShowPrizeModal(false)}
                                >
                                    <Link to="/build">Use in Builder</Link>
                                </Button>
                                <Button
                                    asChild
                                    className="rounded-xl bg-orange-500 hover:bg-orange-600 text-white text-xs font-bold flex items-center justify-center gap-1"
                                    onClick={() => setShowPrizeModal(false)}
                                >
                                    <Link to="/market">
                                        Shop Catalog <ArrowRight className="w-3.5 h-3.5 ml-1" />
                                    </Link>
                                </Button>
                            </div>
                        </div>
                    )}
                </DialogContent>
            </Dialog>
        </div>
    );
}