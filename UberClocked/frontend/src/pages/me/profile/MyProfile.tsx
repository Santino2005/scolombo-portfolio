import { MyProfileForm } from "@/components/common/profile/form/MyProfileForm";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useMyProfile } from "./MyProfile.hooks";

export default function MyProfilePage() {
    const navigate = useNavigate();
    const { profile, loading, saveProfile, deleteAccount, isAuthenticated } = useMyProfile();
    const [open, setOpen] = useState(true);

    if (!isAuthenticated) {
        return (
            <div className="min-h-screen bg-zinc-950 flex items-center justify-center p-6 text-white">
                <p className="text-sm text-zinc-400">You must log in to view your profile.</p>
            </div>
        );
    }

    if (!profile) {
        return (
            <div className="min-h-screen bg-zinc-950 flex items-center justify-center p-6 text-white space-y-3">
                <div className="w-8 h-8 border-2 border-orange-500 border-t-transparent rounded-full animate-spin" />
            </div>
        );
    }

    if (!open) return null;

    return (
        <div className="fixed inset-0 bg-black/80 backdrop-blur-md flex items-center justify-center z-50 p-4">
            <MyProfileForm
                profile={profile}
                loading={loading}
                onSave={saveProfile}
                onDelete={deleteAccount}
                onCancel={() => {
                    setOpen(false);
                    navigate(-1);
                }}
            />
        </div>
    );
}
