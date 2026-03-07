import { dirname } from 'path';
import { fileURLToPath } from 'url';
import { FlatCompat } from '@eslint/eslintrc';
import tseslint from '@typescript-eslint/eslint-plugin';
import tsParser from '@typescript-eslint/parser';
import unusedImports from 'eslint-plugin-unused-imports';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const compat = new FlatCompat({
  baseDirectory: __dirname,
});

const lintConfig = [
  // Ignorar carpetas de build y deps
  {
    ignores: ['node_modules/', '.next/', 'dist/', 'coverage/', 'next-env.d.ts'],
  },

  // Presets oficiales de Next (incluye core-web-vitals y TS)
  ...compat.extends('next/core-web-vitals', 'next/typescript'),

  // Capa TypeScript-ESLint para mejoras de calidad en TS
  {
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      parser: tsParser,
      parserOptions: {
        ecmaVersion: 'latest',
        sourceType: 'module',
        project: false,
      },
    },
    plugins: {
      '@typescript-eslint': tseslint,
      'unused-imports': unusedImports,
    },
    rules: {
      'unused-imports/no-unused-imports': 'warn',
      'unused-imports/no-unused-vars': [
        'warn',
        { vars: 'all', varsIgnorePattern: '^', args: 'after-used', argsIgnorePattern: '^' },
      ],
      '@typescript-eslint/no-unused-vars': [
        'warn',
        { argsIgnorePattern: '^', varsIgnorePattern: '^' },
      ],
      '@typescript-eslint/consistent-type-imports': ['warn', { prefer: 'type-imports' }],
    },
  },

  // Desactivar reglas de estilo que chocan con Prettier
  ...compat.extends('prettier'),
];

export default lintConfig;
