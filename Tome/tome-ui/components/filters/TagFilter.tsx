'use client';

import { useState } from 'react';
import { Plus } from 'lucide-react';
import { Chip } from '@/components/common/Chip';
import { TagFilterModal } from './TagFilterModal';
import { Button } from '@/components/ui/button';

interface TagFilterProps {
  selectedTags: string[];
  onSelectedTagsChange: (tags: string[]) => void;
  className?: string;
}

export function TagFilter({ selectedTags, onSelectedTagsChange, className }: TagFilterProps) {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleTagToggle = (tag: string) => {
    const isCurrentlySelected = selectedTags.includes(tag);

    if (isCurrentlySelected) {
      onSelectedTagsChange(selectedTags.filter((t) => t !== tag));
    } else {
      onSelectedTagsChange([...selectedTags, tag]);
    }
  };

  const handleRemoveTag = (tag: string) => {
    onSelectedTagsChange(selectedTags.filter((t) => t !== tag));
  };

  return (
    <>
      <div className={className}>
        <div className="mb-4 flex items-center justify-between">
          <h3 className="text-lg font-medium text-[#532713]">Tag filter</h3>
          <Button
            variant="ghost"
            size="icon"
            onClick={() => setIsModalOpen(true)}
            className="h-10 w-10 rounded-full border-2 border-[#532713] hover:bg-[#532713]/5"
            aria-label="Add tags"
          >
            <Plus size={24} className="text-[#532713]" />
          </Button>
        </div>

        <div className="flex flex-wrap items-center gap-2">
          {selectedTags.map((tag) => (
            <Chip key={tag} label={tag} selected={true} onRemove={() => handleRemoveTag(tag)} />
          ))}
          {selectedTags.length === 0 && (
            <p className="text-sm text-[#532713] opacity-50">
              No tags selected. Click + to add tags.
            </p>
          )}
        </div>
      </div>

      <TagFilterModal
        open={isModalOpen}
        onOpenChange={setIsModalOpen}
        selectedTags={selectedTags}
        onTagToggle={handleTagToggle}
      />
    </>
  );
}
