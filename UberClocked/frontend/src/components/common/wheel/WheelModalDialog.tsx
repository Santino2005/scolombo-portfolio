import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "@/components/ui/dialog";
import DiscountWheel from "@/components/common/wheel/DiscountWheel";
import { spinWheel, type WheelSpinResponse } from "@/services/wheelApi";
import { Sparkles, Trophy } from "lucide-react";

interface WheelModalDialogProps {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    getToken: () => Promise<string>;
    canSpin?: boolean;
    secondsRemaining?: number | null;
    onSpinComplete?: () => void;
}

export default function WheelModalDialog({
    open,
    onOpenChange,
    getToken,
    canSpin = true,
    secondsRemaining = null,
    onSpinComplete,
}: WheelModalDialogProps) {
    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent className="sm:max-w-lg bg-zinc-950/95 border border-orange-500/40 text-white shadow-2xl p-6 rounded-3xl backdrop-blur-xl">
                <DialogHeader className="text-center space-y-1">
                    <div className="mx-auto inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-orange-500/10 border border-orange-500/30 text-orange-400 text-xs font-black uppercase tracking-widest mb-1">
                        <Sparkles className="w-3.5 h-3.5" /> Daily Reward Wheel
                    </div>
                    <DialogTitle className="text-2xl sm:text-3xl font-black text-transparent bg-clip-text bg-gradient-to-r from-orange-400 via-amber-300 to-orange-500 tracking-tight">
                        Spin & Save Big
                    </DialogTitle>
                    <DialogDescription className="text-zinc-400 text-xs sm:text-sm">
                        Get up to 50% discount on your next hardware or custom PC build!
                    </DialogDescription>
                </DialogHeader>

                <div className="py-2">
                    <DiscountWheel
                        canSpin={canSpin}
                        secondsRemaining={secondsRemaining}
                        onSpin={async () => {
                            const res = await spinWheel(getToken);
                            if (onSpinComplete) onSpinComplete();
                            return res;
                        }}
                    />
                </div>
            </DialogContent>
        </Dialog>
    );
}
