import type { UserDataDto } from "@/types/UserDataDto";
import { useState } from "react";
import { User, Mail, Globe, Phone, Shield, Palette, Trash2, X, Check, AlertTriangle } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "@/components/ui/dialog";
import { useTheme, ACCENT_CONFIG, type AccentColor } from "@/context/ThemeContext";

interface MyProfileFormProps {
    profile: UserDataDto;
    loading: boolean;
    onSave: (data: UserDataDto) => void;
    onDelete: () => void;
    onCancel: () => void;
}

export function MyProfileForm({
    profile,
    loading,
    onSave,
    onDelete,
    onCancel,
}: MyProfileFormProps) {
    const [form, setForm] = useState<UserDataDto>(profile);
    const { accent, setAccent, mode, toggleMode } = useTheme();
    const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);

    return (
        <>
            <div className="w-full max-w-lg rounded-3xl bg-zinc-950 border border-zinc-800 text-white shadow-2xl p-6 sm:p-8 space-y-6 relative overflow-hidden backdrop-blur-2xl">
                {/* Header */}
                <div className="flex items-center justify-between pb-4 border-b border-zinc-850">
                    <div className="flex items-center gap-3">
                        <div className="w-12 h-12 rounded-2xl bg-gradient-to-br from-orange-500 to-amber-500 flex items-center justify-center text-white shadow-lg shadow-orange-500/20 font-black text-lg">
                            {form.userName ? form.userName.slice(0, 2).toUpperCase() : <User className="w-6 h-6" />}
                        </div>
                        <div>
                            <h2 className="text-xl font-black text-white tracking-tight">Account Profile</h2>
                            <p className="text-xs text-zinc-400 font-medium">Manage your personal details & theme preferences</p>
                        </div>
                    </div>

                    <button
                        type="button"
                        onClick={onCancel}
                        className="p-1.5 rounded-xl bg-zinc-900 border border-zinc-800 text-zinc-400 hover:text-white transition"
                    >
                        <X className="w-5 h-5" />
                    </button>
                </div>

                {/* Form Fields */}
                <div className="space-y-4">
                    {/* Username */}
                    <div className="space-y-1.5">
                        <label className="text-xs font-bold text-zinc-300 uppercase tracking-wider flex items-center gap-1.5">
                            <User className="w-3.5 h-3.5 text-orange-400" /> Username
                        </label>
                        <Input
                            value={form.userName ?? ""}
                            onChange={(e) => setForm({ ...form, userName: e.target.value })}
                            className="bg-zinc-900/90 border-zinc-800 text-white placeholder:text-zinc-500 rounded-xl text-sm focus:border-orange-500"
                            placeholder="Enter your username"
                        />
                    </div>

                    {/* Email (Readonly) */}
                    <div className="space-y-1.5">
                        <label className="text-xs font-bold text-zinc-400 uppercase tracking-wider flex items-center gap-1.5">
                            <Mail className="w-3.5 h-3.5 text-zinc-500" /> Email Address
                        </label>
                        <Input
                            value={form.email ?? ""}
                            disabled
                            className="bg-zinc-900/50 border-zinc-800/80 text-zinc-400 cursor-not-allowed rounded-xl text-sm font-mono"
                        />
                    </div>

                    {/* Country & Phone */}
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                        <div className="space-y-1.5">
                            <label className="text-xs font-bold text-zinc-300 uppercase tracking-wider flex items-center gap-1.5">
                                <Globe className="w-3.5 h-3.5 text-orange-400" /> Country
                            </label>
                            <Input
                                value={form.country ?? ""}
                                onChange={(e) => setForm({ ...form, country: e.target.value })}
                                className="bg-zinc-900/90 border-zinc-800 text-white placeholder:text-zinc-500 rounded-xl text-sm focus:border-orange-500"
                                placeholder="e.g. Argentina, USA"
                            />
                        </div>

                        <div className="space-y-1.5">
                            <label className="text-xs font-bold text-zinc-300 uppercase tracking-wider flex items-center gap-1.5">
                                <Phone className="w-3.5 h-3.5 text-orange-400" /> Cellphone
                            </label>
                            <Input
                                value={form.cellPhone ?? ""}
                                onChange={(e) => setForm({ ...form, cellPhone: e.target.value })}
                                className="bg-zinc-900/90 border-zinc-800 text-white placeholder:text-zinc-500 rounded-xl text-sm focus:border-orange-500"
                                placeholder="+1 234 567 890"
                            />
                        </div>
                    </div>

                    {/* Theme & Accent Color Preference Section */}
                    <div className="p-4 rounded-2xl bg-zinc-900/60 border border-zinc-800/80 space-y-3">
                        <div className="flex items-center justify-between">
                            <span className="text-xs font-bold text-zinc-300 uppercase tracking-wider flex items-center gap-1.5">
                                <Palette className="w-3.5 h-3.5 text-orange-400" /> Theme Accent Color
                            </span>
                            <button
                                type="button"
                                onClick={toggleMode}
                                className="text-[11px] font-bold text-orange-400 hover:underline"
                            >
                                Mode: {mode.toUpperCase()}
                            </button>
                        </div>

                        <div className="grid grid-cols-3 sm:grid-cols-6 gap-2">
                            {(Object.keys(ACCENT_CONFIG) as AccentColor[]).map((key) => {
                                const cfg = ACCENT_CONFIG[key];
                                const isSelected = accent === key;
                                return (
                                    <button
                                        key={key}
                                        type="button"
                                        onClick={() => setAccent(key)}
                                        className={`p-2 rounded-xl border flex flex-col items-center gap-1 transition ${
                                            isSelected
                                                ? "bg-zinc-800 border-white/40 shadow-md"
                                                : "bg-zinc-950/60 border-zinc-800 hover:bg-zinc-900"
                                        }`}
                                    >
                                        <span
                                            className="w-4 h-4 rounded-full flex items-center justify-center"
                                            style={{ backgroundColor: cfg.hex }}
                                        >
                                            {isSelected && <Check className="w-2.5 h-2.5 text-white" />}
                                        </span>
                                        <span className="text-[9px] font-bold capitalize text-zinc-400">{key}</span>
                                    </button>
                                );
                            })}
                        </div>
                    </div>
                </div>

                {/* Actions */}
                <div className="flex flex-col-reverse sm:flex-row items-center justify-between gap-3 pt-4 border-t border-zinc-850">
                    <button
                        type="button"
                        onClick={() => setShowDeleteConfirm(true)}
                        className="text-xs text-red-400 hover:text-red-300 font-semibold flex items-center gap-1 py-2"
                    >
                        <Trash2 className="w-3.5 h-3.5" /> Delete Account
                    </button>

                    <div className="flex items-center gap-3 w-full sm:w-auto">
                        <Button
                            variant="outline"
                            onClick={onCancel}
                            className="flex-1 sm:flex-none rounded-xl border-zinc-800 bg-zinc-900 hover:bg-zinc-800 text-zinc-300 hover:text-white text-xs font-bold"
                        >
                            Cancel
                        </Button>
                        <Button
                            onClick={() => onSave(form)}
                            disabled={loading}
                            className="flex-1 sm:flex-none rounded-xl bg-gradient-to-r from-orange-500 to-amber-500 hover:from-orange-600 hover:to-amber-600 text-white text-xs font-extrabold px-6 shadow-lg shadow-orange-500/20"
                        >
                            {loading ? "Saving..." : "Save Changes"}
                        </Button>
                    </div>
                </div>
            </div>

            {/* Confirmation Modal Popup for Account Deletion */}
            <Dialog open={showDeleteConfirm} onOpenChange={setShowDeleteConfirm}>
                <DialogContent className="sm:max-w-md bg-zinc-950 border border-red-500/40 text-white shadow-2xl p-6 rounded-3xl">
                    <DialogHeader className="text-center space-y-2">
                        <div className="mx-auto w-12 h-12 rounded-full bg-red-500/20 border border-red-500/30 flex items-center justify-center text-red-400">
                            <AlertTriangle className="w-6 h-6" />
                        </div>
                        <DialogTitle className="text-xl font-bold text-white">Delete Account?</DialogTitle>
                        <DialogDescription className="text-zinc-400 text-xs">
                            This action cannot be undone. All your custom PC configurations, cart items, and order records will be permanently removed.
                        </DialogDescription>
                    </DialogHeader>

                    <div className="flex items-center justify-end gap-3 pt-4 border-t border-zinc-900">
                        <Button
                            variant="outline"
                            className="rounded-xl border-zinc-800 bg-zinc-900 text-xs"
                            onClick={() => setShowDeleteConfirm(false)}
                        >
                            Cancel
                        </Button>
                        <Button
                            className="rounded-xl bg-red-600 hover:bg-red-700 text-white text-xs font-bold"
                            onClick={() => {
                                setShowDeleteConfirm(false);
                                onDelete();
                            }}
                        >
                            Confirm Delete
                        </Button>
                    </div>
                </DialogContent>
            </Dialog>
        </>
    );
}
