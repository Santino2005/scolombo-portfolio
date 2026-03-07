import { Search } from 'lucide-react';

interface SearchInputProps {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
  onClear?: () => void;
  disabled?: boolean;
}

export function SearchInput({
  value,
  onChange,
  placeholder = 'Search by title, author or ISBN',
  onClear,
  disabled = false,
}: SearchInputProps) {
  return (
    <div className="relative w-full">
      <Search
        className={`absolute top-1/2 left-4 h-5 w-5 -translate-y-1/2 transform ${
          disabled ? 'text-gray-400' : 'text-muted-foreground'
        }`}
      />
      <input
        type="search"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder}
        disabled={disabled}
        className={`border-border w-full rounded-lg border py-3 pr-12 pl-12 text-base transition-all focus:outline-none ${
          disabled
            ? 'cursor-default border-gray-200 bg-gray-100 text-gray-500'
            : 'bg-card focus:ring-ring focus:border-transparent focus:ring-2'
        }`}
      />
      {value && onClear && !disabled && (
        <button
          onClick={onClear}
          className="text-muted-foreground hover:text-foreground absolute top-1/2 right-4 -translate-y-1/2 transform text-lg transition-colors"
          type="button"
        >
          ✕
        </button>
      )}
    </div>
  );
}
