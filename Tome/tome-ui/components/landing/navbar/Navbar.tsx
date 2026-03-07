import Link from 'next/link';
import Image from 'next/image';
import { NavigationMenu, NavigationMenuLink, NavigationMenuList } from '../../ui/navigation-menu';
import { Button } from '../../ui/button';
import { useRouter } from 'next/navigation';

function NavBar() {
  const router = useRouter();
  return (
    <div className="sticky top-0 z-50 w-full border-b border-[var(--ds-neutral-100)]/30 bg-[var(--ds-neutral-100)]/20 shadow-sm backdrop-blur-md">
      <div className="flex h-16 items-center justify-between px-6">
        {/* Logo */}
        <div className="relative h-[53px] w-[183px]">
          <Image
            src="/TomeLogoH.png"
            alt="TomeLogo"
            fill
            className="object-contain"
            sizes={'(max-width: 640px) 100vw, 183px'}
          />
        </div>

        {/* Navigation */}
        <NavigationMenu className="flex-1 justify-center" viewport={false}>
          <NavigationMenuList className="flex gap-8">
            <NavigationMenuLink asChild>
              <Link href="#home">Home</Link>
            </NavigationMenuLink>
            <NavigationMenuLink asChild>
              <Link href="#overview">Overview</Link>
            </NavigationMenuLink>
            <NavigationMenuLink asChild>
              <Link href="#features">Features</Link>
            </NavigationMenuLink>
          </NavigationMenuList>
        </NavigationMenu>

        {/* Buttons */}
        <div className="flex items-center gap-4">
          <Button onClick={() => router.push('/auth/login')} variant={'neutral_link'}>
            Login
          </Button>
          <Button onClick={() => router.push('auth/login?screen_hint=signup')} variant={'neutral'}>
            Join the club
          </Button>
        </div>
      </div>
    </div>
  );
}

export default NavBar;
