interface SearchInputProps {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
  onClear?: () => void;
  disabled?: boolean;
}

export function CreateInput({
  value,
  onChange,
  placeholder = 'Name',
  onClear,
  disabled = false,
}: SearchInputProps) {
  return (
    <div className="relative w-full">
      <input
        type="search"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder}
        disabled={disabled}
        className={`border-border w-full rounded-lg border py-3 pl-4 text-base transition-all focus:outline-none ${
          disabled
            ? 'cursor-not-allowed border-gray-200 bg-gray-100 text-gray-500'
            : 'bg-card focus:ring-ring focus:border-transparent focus:ring-2'
        }`}
      />
      {value && onClear && !disabled && (
        <button
          onClick={onClear}
          className="text-muted-foreground hover:text-foreground absolute top-1/2 z-10 -translate-y-1/2 transform text-lg transition-colors"
          type="button"
        >
          ✕
        </button>
      )}
    </div>
  );
}
