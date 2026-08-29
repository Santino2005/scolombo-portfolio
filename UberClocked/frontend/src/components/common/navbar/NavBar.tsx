import { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { useAuth0 } from "@auth0/auth0-react";
import {
    Cpu,
    ShoppingBag,
    MessageSquare,
    Layers,
    Gift,
    Shield,
    Menu,
    X,
    User,
    LogOut,
    ShoppingCart,
    Flame,
    Sparkles,
    CreditCard,
    TicketPercent
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import logo from "../../../stories/assets/uberClocked(Only_logo).png";
import { useNavBarLogic } from "./NavBar.hook";
import WheelModalDialog from "@/components/common/wheel/WheelModalDialog";

export default function NavBar() {
    const { loginWithRedirect, isAuthenticated, isLoading, isAdmin } = useNavBarLogic();
    const { user, logout, getAccessTokenSilently } = useAuth0();
    const location = useLocation();
    const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
    const [wheelModalOpen, setWheelModalOpen] = useState(false);

    if (isLoading) return null;

    const isActive = (path: string) => {
        if (path === "/" && location.pathname === "/") return true;
        if (path !== "/" && location.pathname.startsWith(path)) return true;
        return false;
    };

    const navLinks = [
        { label: "Home", to: "/", icon: Flame },
        { label: "PC Builder", to: "/build", icon: Cpu, highlight: true },
        { label: "Market", to: "/market", icon: ShoppingBag },
        { label: "Community", to: "/posts", icon: MessageSquare },
    ];

    const userLinks = [
        { label: "My Purchases", to: "/purchases", icon: CreditCard },
        { label: "My Coupons", to: "/coupons", icon: TicketPercent },
        { label: "Profile", to: "/profile", icon: User },
    ];

    const adminLinks = [
        { label: "Components", to: "/admin/components" },
        { label: "Products", to: "/admin/products" },
        { label: "Coupons", to: "/admin/promotions" },
        { label: "Purchases", to: "/admin/purchases" },
        { label: "Reviews", to: "/admin/reviews" },
        { label: "Companies", to: "/admin/companies" },
        { label: "Posts (Admin)", to: "/admin/posts" },
    ];

    const closeMobile = () => setMobileMenuOpen(false);

    return (
        <>
            <nav className="sticky top-0 z-50 w-full bg-zinc-950/85 backdrop-blur-xl border-b border-zinc-800/80 shadow-2xl transition-all">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex items-center justify-between h-16 sm:h-20">
                        {/* Brand Logo & Name */}
                        <Link to="/" className="flex items-center gap-3 group shrink-0" onClick={closeMobile}>
                            <div className="relative flex items-center justify-center">
                                <div className="absolute -inset-1 bg-gradient-to-r from-orange-500 to-amber-500 rounded-full blur opacity-40 group-hover:opacity-80 transition duration-300" />
                                <img
                                    src={logo}
                                    alt="UberClocked Logo"
                                    className="relative h-10 w-10 sm:h-12 sm:w-12 object-contain drop-shadow"
                                />
                            </div>
                            <div className="flex flex-col">
                                <span className="text-lg sm:text-xl font-black tracking-tight text-white group-hover:text-orange-400 transition">
                                    Über<span className="text-orange-500">Clocked</span>
                                </span>
                                <span className="text-[9px] uppercase tracking-widest font-extrabold text-zinc-400 -mt-1 hidden sm:block">
                                    Hardware & Custom PCs
                                </span>
                            </div>
                        </Link>

                        {/* Desktop Navigation Links */}
                        <div className="hidden lg:flex items-center gap-1 xl:gap-2">
                            {navLinks.map((link) => {
                                const active = isActive(link.to);
                                const Icon = link.icon;

                                if (link.highlight) {
                                    return (
                                        <Link
                                            key={link.to}
                                            to={link.to}
                                            className={`relative px-4 py-2 rounded-xl text-xs xl:text-sm font-extrabold flex items-center gap-1.5 transition-all shadow-md ${
                                                active
                                                    ? "bg-orange-500 text-white shadow-orange-500/30"
                                                    : "bg-orange-500/10 text-orange-400 hover:bg-orange-500 hover:text-white border border-orange-500/30"
                                            }`}
                                        >
                                            <Icon className="w-4 h-4" />
                                            {link.label}
                                        </Link>
                                    );
                                }

                                return (
                                    <Link
                                        key={link.to}
                                        to={link.to}
                                        className={`relative px-3.5 py-2 rounded-xl text-xs xl:text-sm font-bold flex items-center gap-1.5 transition-all ${
                                            active
                                                ? "text-white bg-zinc-900 border border-zinc-800 shadow-inner"
                                                : "text-zinc-300 hover:text-white hover:bg-zinc-900/60"
                                        }`}
                                    >
                                        <Icon className={`w-4 h-4 ${active ? "text-orange-400" : "text-zinc-400"}`} />
                                        {link.label}
                                    </Link>
                                );
                            })}

                            {/* Wheel Modal Trigger Button */}
                            <button
                                type="button"
                                onClick={() => setWheelModalOpen(true)}
                                className="px-3.5 py-2 rounded-xl text-xs xl:text-sm font-bold text-amber-300 hover:text-white bg-amber-500/10 hover:bg-amber-500/20 border border-amber-500/30 flex items-center gap-1.5 transition-all animate-pulse"
                            >
                                <Sparkles className="w-4 h-4 text-amber-400" />
                                Lucky Wheel
                            </button>

                            {/* Admin Menu Dropdown */}
                            {isAdmin && (
                                <DropdownMenu>
                                    <DropdownMenuTrigger asChild>
                                        <button className="px-3 py-2 rounded-xl text-xs xl:text-sm font-bold text-purple-300 bg-purple-500/10 hover:bg-purple-500/20 border border-purple-500/30 flex items-center gap-1.5 transition">
                                            <Shield className="w-4 h-4 text-purple-400" />
                                            Admin Panel ▾
                                        </button>
                                    </DropdownMenuTrigger>
                                    <DropdownMenuContent align="end" className="w-52 bg-zinc-950 border border-zinc-800 text-zinc-200 shadow-2xl p-1 rounded-xl">
                                        <div className="px-3 py-1.5 text-[11px] font-bold uppercase tracking-wider text-purple-400">
                                            Administrator Controls
                                        </div>
                                        <DropdownMenuSeparator className="bg-zinc-800" />
                                        {adminLinks.map((a) => (
                                            <DropdownMenuItem key={a.to} asChild className="hover:bg-zinc-900 text-xs font-semibold py-2 rounded-lg cursor-pointer">
                                                <Link to={a.to}>{a.label}</Link>
                                            </DropdownMenuItem>
                                        ))}
                                    </DropdownMenuContent>
                                </DropdownMenu>
                            )}
                        </div>

                        {/* Right Actions: Cart & User Avatar / Login */}
                        <div className="flex items-center gap-3">
                            {/* Cart Icon Link */}
                            {isAuthenticated && (
                                <Link
                                    to="/cart"
                                    className={`relative p-2.5 rounded-xl border transition-all flex items-center justify-center ${
                                        isActive("/cart")
                                            ? "bg-orange-500 text-white border-orange-500 shadow-lg shadow-orange-500/30"
                                            : "bg-zinc-900/80 text-zinc-200 border-zinc-800 hover:border-orange-500/50 hover:bg-zinc-900"
                                    }`}
                                    title="View Cart"
                                >
                                    <ShoppingCart className="w-5 h-5" />
                                </Link>
                            )}

                            {/* User Authentication Menu */}
                            {isAuthenticated ? (
                                <DropdownMenu>
                                    <DropdownMenuTrigger asChild>
                                        <button className="flex items-center gap-2.5 p-1.5 pr-3 rounded-xl bg-zinc-900/80 border border-zinc-800 hover:border-zinc-700 transition focus:outline-none">
                                            <Avatar className="h-8 w-8 rounded-lg border border-orange-500/40">
                                                <AvatarImage src={user?.picture} />
                                                <AvatarFallback className="bg-zinc-800 text-orange-400 font-bold text-xs">
                                                    {user?.name?.slice(0, 2).toUpperCase() ?? <User className="w-4 h-4" />}
                                                </AvatarFallback>
                                            </Avatar>
                                            <span className="text-xs font-bold text-white max-w-[100px] truncate hidden sm:block">
                                                {user?.name ?? "My Account"}
                                            </span>
                                        </button>
                                    </DropdownMenuTrigger>
                                    <DropdownMenuContent align="end" className="w-56 bg-zinc-950 border border-zinc-800 text-zinc-200 shadow-2xl p-1.5 rounded-2xl">
                                        <div className="p-2 border-b border-zinc-800">
                                            <p className="text-xs font-bold text-white truncate">{user?.name}</p>
                                            <p className="text-[11px] text-zinc-400 truncate">{user?.email}</p>
                                        </div>

                                        <div className="py-1">
                                            {userLinks.map((ul) => {
                                                const Icon = ul.icon;
                                                return (
                                                    <DropdownMenuItem key={ul.to} asChild className="hover:bg-zinc-900 text-xs font-semibold py-2 rounded-xl cursor-pointer">
                                                        <Link to={ul.to} className="flex items-center gap-2">
                                                            <Icon className="w-4 h-4 text-orange-400" />
                                                            <span>{ul.label}</span>
                                                        </Link>
                                                    </DropdownMenuItem>
                                                );
                                            })}
                                        </div>

                                        <DropdownMenuSeparator className="bg-zinc-800" />

                                        <DropdownMenuItem
                                            onClick={() => logout({ logoutParams: { returnTo: window.location.origin } })}
                                            className="hover:bg-red-500/10 text-red-400 hover:text-red-300 text-xs font-semibold py-2 rounded-xl cursor-pointer flex items-center gap-2"
                                        >
                                            <LogOut className="w-4 h-4" />
                                            <span>Sign Out</span>
                                        </DropdownMenuItem>
                                    </DropdownMenuContent>
                                </DropdownMenu>
                            ) : (
                                <Button
                                    size="sm"
                                    className="rounded-xl bg-gradient-to-r from-orange-500 to-amber-500 hover:from-orange-600 hover:to-amber-600 text-white font-bold text-xs px-4 shadow-lg shadow-orange-500/25 border border-orange-400/30"
                                    onClick={() =>
                                        loginWithRedirect({
                                            authorizationParams: { redirect_uri: window.location.origin + "/auth-callback" },
                                        })
                                    }
                                >
                                    Sign In / Register
                                </Button>
                            )}

                            {/* Mobile Hamburger Toggle */}
                            <button
                                type="button"
                                className="lg:hidden p-2 rounded-xl bg-zinc-900 border border-zinc-800 text-zinc-300 hover:text-white"
                                onClick={() => setMobileMenuOpen((prev) => !prev)}
                                aria-label="Toggle navigation menu"
                            >
                                {mobileMenuOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
                            </button>
                        </div>
                    </div>
                </div>

                {/* Mobile Drawer Menu */}
                {mobileMenuOpen && (
                    <div className="lg:hidden bg-zinc-950/98 border-b border-zinc-800 px-4 pt-3 pb-6 space-y-2 backdrop-blur-2xl animate-in slide-in-from-top-4 duration-200">
                        <div className="grid grid-cols-2 gap-2 pb-2">
                            {navLinks.map((link) => {
                                const active = isActive(link.to);
                                const Icon = link.icon;
                                return (
                                    <Link
                                        key={link.to}
                                        to={link.to}
                                        onClick={closeMobile}
                                        className={`flex items-center gap-2.5 p-3 rounded-xl text-xs font-bold transition-all ${
                                            active
                                                ? "bg-orange-500 text-white shadow-md shadow-orange-500/30"
                                                : "bg-zinc-900/90 text-zinc-300 border border-zinc-800/80"
                                        }`}
                                    >
                                        <Icon className="w-4 h-4" />
                                        <span>{link.label}</span>
                                    </Link>
                                );
                            })}
                        </div>

                        <button
                            type="button"
                            onClick={() => {
                                closeMobile();
                                setWheelModalOpen(true);
                            }}
                            className="w-full flex items-center justify-center gap-2 p-3 rounded-xl bg-amber-500/10 border border-amber-500/30 text-amber-300 text-xs font-extrabold"
                        >
                            <Sparkles className="w-4 h-4 text-amber-400" />
                            Daily Discount Wheel
                        </button>

                        {isAuthenticated && (
                            <div className="pt-2 border-t border-zinc-900 space-y-1">
                                <div className="text-[11px] font-bold uppercase tracking-wider text-zinc-400 px-1 py-1">
                                    My Account
                                </div>
                                <div className="grid grid-cols-2 gap-1.5">
                                    {userLinks.map((ul) => {
                                        const Icon = ul.icon;
                                        return (
                                            <Link
                                                key={ul.to}
                                                to={ul.to}
                                                onClick={closeMobile}
                                                className="flex items-center gap-2 p-2.5 rounded-lg bg-zinc-900/60 border border-zinc-800/60 text-xs font-medium text-zinc-300"
                                            >
                                                <Icon className="w-3.5 h-3.5 text-orange-400" />
                                                <span>{ul.label}</span>
                                            </Link>
                                        );
                                    })}
                                </div>
                            </div>
                        )}

                        {isAdmin && (
                            <div className="pt-2 border-t border-zinc-900 space-y-1">
                                <div className="text-[11px] font-bold uppercase tracking-wider text-purple-400 px-1 py-1">
                                    Admin Shortcuts
                                </div>
                                <div className="grid grid-cols-2 gap-1.5">
                                    {adminLinks.map((a) => (
                                        <Link
                                            key={a.to}
                                            to={a.to}
                                            onClick={closeMobile}
                                            className="p-2 rounded-lg bg-zinc-900/60 border border-zinc-800/60 text-[11px] font-medium text-purple-300 truncate"
                                        >
                                            {a.label}
                                        </Link>
                                    ))}
                                </div>
                            </div>
                        )}
                    </div>
                )}
            </nav>

            {/* Global Wheel Popup Dialog */}
            <WheelModalDialog
                open={wheelModalOpen}
                onOpenChange={setWheelModalOpen}
                getToken={async () => {
                    if (!isAuthenticated) {
                        await loginWithRedirect();
                        return "";
                    }
                    return getAccessTokenSilently();
                }}
            />
        </>
    );
}