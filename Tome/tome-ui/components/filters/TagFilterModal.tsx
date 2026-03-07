'use client';

import { useState, useEffect } from 'react';
import { Search } from 'lucide-react';
import { Dialog, DialogContent } from '@/components/ui/dialog';
import { Chip } from '@/components/common/Chip';
import { Input } from '@/components/ui/input';
import { useDebounce } from '@/lib/hooks/useDebounce';

interface TagFilterModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  selectedTags: string[];
  onTagToggle: (tag: string) => void;
}

export function TagFilterModal({
  open,
  onOpenChange,
  selectedTags,
  onTagToggle,
}: TagFilterModalProps) {
  const [searchTerm, setSearchTerm] = useState('');
  const [availableTags, setAvailableTags] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);
  const debouncedSearch = useDebounce(searchTerm, 300);

  useEffect(() => {
    async function fetchTags() {
      setLoading(true);
      try {
        const res = await fetch(`/api/tags?search=${encodeURIComponent(debouncedSearch)}`);
        if (res.ok) {
          const data = await res.json();
          setAvailableTags(data.names || []);
        }
      } catch (error) {
        console.error('Error fetching tags:', error);
      } finally {
        setLoading(false);
      }
    }

    if (open) {
      fetchTags();
    }
  }, [debouncedSearch, open]);

  const isSelected = (tag: string) => selectedTags.includes(tag);

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="flex max-h-[80vh] max-w-4xl flex-col overflow-hidden rounded-2xl bg-[#ebe6e0] p-8">
        <div className="flex flex-col gap-6">
          <div className="relative">
            <Search
              className="absolute top-1/2 left-4 -translate-y-1/2 text-[#532713] opacity-50"
              size={20}
            />
            <Input
              type="text"
              placeholder="Search tag by name"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="rounded-full border-0 bg-white py-6 pr-4 pl-12 text-base shadow-sm focus-visible:ring-0 focus-visible:ring-offset-0"
            />
          </div>

          <div className="flex max-h-[50vh] flex-wrap gap-2 overflow-y-auto pr-2">
            {loading ? (
              <p className="w-full py-8 text-center text-[#532713] opacity-60">Loading tags...</p>
            ) : availableTags.length === 0 ? (
              <p className="w-full py-8 text-center text-[#532713] opacity-60">No tags found</p>
            ) : (
              availableTags.map((tag) => (
                <Chip
                  key={tag}
                  label={tag}
                  selected={isSelected(tag)}
                  onClick={() => onTagToggle(tag)}
                  onRemove={isSelected(tag) ? () => onTagToggle(tag) : undefined}
                />
              ))
            )}
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
}
