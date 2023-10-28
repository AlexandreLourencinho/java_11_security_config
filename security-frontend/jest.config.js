const { pathsToModuleNameMapper } = require('ts-jest');
const { compilerOptions } = require('./tsconfig');

module.exports = {
    preset: 'jest-preset-angular',
    roots: ['src'],
    setupFilesAfterEnv: ['<rootDir>/src/setup-jest.ts'],
    globalSetup: 'jest-preset-angular/global-setup',
    coverageDirectory: 'reports',
    coverageThreshold: {
      global: {
        branches: 80,
        functions: 80,
        lines: 80,
        statements: 80,
      },
    },
    moduleNameMapper: {...pathsToModuleNameMapper(compilerOptions.paths || {}, { prefix: '<rootDir>/' })},
    testMatch: ['<rootDir>/src/app/**/*.spec.ts', '!**/node_modules/**', '!**/coverage/**', '!<rootDir>/src/app/shared/_mocks/**'],
    transformIgnorePatterns: ['node_modules/(?!.*\\.mjs$)'],
};
