'use client';

import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  useSidebar,
} from '@/components/ui/sidebar';
import { HomeIcon, LibraryIcon, LogOutIcon, SearchIcon, Plus } from 'lucide-react';
import { useUser } from '@auth0/nextjs-auth0';
import { Avatar, AvatarImage, AvatarFallback } from '@/components/ui/avatar';
import { Button } from '@/components/ui/button';
import { usePathname, useRouter } from 'next/navigation';
import Image from 'next/image';
import * as React from 'react';
import { Tooltip, TooltipContent, TooltipTrigger } from '@/components/ui/tooltip';
import { useUserBookClubs } from '@/lib/hooks/useUserBookClubs';
import { Skeleton } from '@/components/ui/skeleton';

const items = [
  { title: 'Home', href: '/', icon: HomeIcon },
  { title: 'Your library', href: '/library', icon: LibraryIcon },
  { title: 'Search', href: '/search', icon: SearchIcon },
];

export function AppSidebar() {
  const iconSize = 32;
  const { user: authUser, isLoading } = useUser();
  const { setOpen } = useSidebar();
  const pathname = usePathname();
  const router = useRouter();

  const displayName = authUser?.name;
  const avatarUrl = authUser?.picture;
  const initials = (displayName || '')
    .split(/\s+/)
    .map((p) => p[0])
    .join('')
    .slice(0, 2)
    .toUpperCase();

  const handleLogout = () => (window.location.href = '/auth/logout');
  const { bookClubs, loading, error } = useUserBookClubs();

  // Estado para las imágenes de los clubes
  const [clubImages, setClubImages] = React.useState<Record<string, string>>(() =>
    bookClubs.reduce(
      (acc, club) => {
        acc[club.id] = club.imageBase64?.trim() ? club.imageBase64 : '/images/default-club.png';
        return acc;
      },
      {} as Record<string, string>,
    ),
  );

  React.useEffect(() => {
    const updatedImages: Record<string, string> = {};
    bookClubs.forEach((club) => {
      updatedImages[club.id] = club.imageBase64?.trim()
        ? club.imageBase64
        : '/images/default-club.png';
    });
    setClubImages(updatedImages);
  }, [bookClubs]);

  const handleImgError = (clubId: string) => {
    setClubImages((prev) => ({ ...prev, [clubId]: '/images/default-club.png' }));
  };

  return (
    <Sidebar
      collapsible="icon"
      className="bg-sidebar"
      onMouseEnter={() => setOpen(true)}
      onMouseLeave={() => setOpen(false)}
    >
      <SidebarHeader>
        <SidebarMenu>
          {items.map((item) => {
            const isActive = pathname === item.href;
            return (
              <SidebarMenuItem key={item.title} onClick={() => router.push(item.href)}>
                <SidebarMenuButton size="xl" className="cursor-pointer gap-3 rounded-xl pl-0">
                  <div
                    className={`flex size-14 shrink-0 items-center justify-center rounded-xl border-2 transition-colors ${
                      isActive
                        ? 'border-sidebar-primary bg-sidebar-primary'
                        : 'border-sidebar-primary bg-background'
                    }`}
                  >
                    <item.icon
                      size={iconSize}
                      className={`transition-colors ${isActive ? 'text-background' : 'text-sidebar-primary'}`}
                    />
                  </div>
                  <span className="u-text-title-large text-sm">{item.title}</span>
                </SidebarMenuButton>
              </SidebarMenuItem>
            );
          })}
        </SidebarMenu>
      </SidebarHeader>

      <SidebarContent>
        <SidebarGroup>
          <SidebarMenu>
            <SidebarMenuItem>
              <SidebarMenuButton
                size="xl"
                className="hover:bg-sidebar-accent cursor-pointer gap-3 rounded-full pl-0"
                onClick={() => router.push('/book-clubs/create')}
              >
                <div className="bg-muted-foreground text-sidebar-primary-foreground flex size-14 shrink-0 items-center justify-center rounded-[400px]">
                  <Plus className="text-sidebar-accent-foreground h-6 w-6" />
                </div>
                <span className="u-text-title-large text-sm text-current group-data-[collapsible=icon]:hidden">
                  Create Club
                </span>
              </SidebarMenuButton>
            </SidebarMenuItem>
            {loading && (
              <>
                {[1, 2, 3].map((i) => (
                  <SidebarMenuItem key={`skeleton-${i}`}>
                    <SidebarMenuButton
                      size="xl"
                      className="cursor-default gap-3 rounded-full pl-0"
                      aria-hidden
                      tabIndex={-1}
                    >
                      <div className="border-sidebar bg-sidebar-primary relative size-14 shrink-0 overflow-hidden rounded-full border">
                        <Skeleton className="bg-secondary h-14 w-14 rounded-full" />
                      </div>
                      <Skeleton className="bg-muted-foreground ml-2 h-4 w-28 rounded-md group-data-[collapsible=icon]:hidden" />
                    </SidebarMenuButton>
                  </SidebarMenuItem>
                ))}
              </>
            )}
            {error && <span className="text-destructive ml-4 text-sm">{error}</span>}
            {!loading &&
              bookClubs.map((club) => (
                <SidebarMenuItem key={club.id}>
                  <SidebarMenuButton
                    size="xl"
                    className="hover:bg-sidebar-accent cursor-pointer gap-3 rounded-full pl-0"
                    onClick={() => router.push(`/book-clubs/${club.id}`)}
                  >
                    <div className="border-sidebar-primary bg-background relative size-14 shrink-0 overflow-hidden rounded-full border">
                      <Image
                        src={clubImages[club.id] || '/images/default-club.png'}
                        alt={club.details || 'Club'}
                        fill
                        sizes="56px"
                        className="rounded-full object-cover"
                        onError={() => handleImgError(club.id)}
                      />
                    </div>
                    <span className="u-text-title-large text-sm text-current group-data-[collapsible=icon]:hidden">
                      {club.details}
                    </span>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
          </SidebarMenu>
        </SidebarGroup>
      </SidebarContent>

      <SidebarFooter>
        <SidebarMenu>
          <SidebarMenuItem>
            <div className="flex items-center gap-3">
              <Avatar className="bg-sidebar-primary text-sidebar-primary-foreground flex size-14 items-center justify-center rounded-xl">
                {!isLoading && avatarUrl && <AvatarImage src={avatarUrl} alt={displayName} />}
                <AvatarFallback className="u-text-title-medium bg-sidebar-primary text-sidebar-primary-foreground rounded-xl">
                  {isLoading ? '…' : initials}
                </AvatarFallback>
              </Avatar>

              <Tooltip>
                <TooltipTrigger asChild>
                  <span
                    tabIndex={0}
                    aria-label={displayName}
                    title={displayName}
                    className="u-text-title-medium max-w-[140px] min-w-0 flex-1 truncate text-[var(--ds-neutral-800)] group-data-[collapsible=icon]:hidden"
                  >
                    {displayName}
                  </span>
                </TooltipTrigger>
                <TooltipContent sideOffset={6}>{displayName}</TooltipContent>
              </Tooltip>

              <Button
                type="button"
                variant="ghost"
                size="icon"
                aria-label="Logout"
                onClick={handleLogout}
                className="text-destructive hover:bg-sidebar-accent hover:text-sidebar-accent-foreground size-12 rounded-xl bg-transparent group-data-[collapsible=icon]:hidden"
              >
                <LogOutIcon className="size-6" />
              </Button>
            </div>
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarFooter>
    </Sidebar>
  );
}
