---
name: vscode-problems-diagnosis
description: "Diagnose and fix VS Code Problems panel errors and warnings. Use when a user reports an error, warning, red squiggle, diagnostic, or problem console issue in any language or workspace."
argument-hint: "Describe the Problems panel message, file, line, and expected behavior."
user-invocable: true
disable-model-invocation: false
---

# VS Code Problems Diagnosis

## Purpose

Resolve a VS Code Problems panel diagnostic at its source, with the smallest behavior-preserving change and a focused validation. Treat the Problems panel as an entry point to a compiler, linter, language server, or build diagnostic rather than as the root cause itself.

## When to Use

Use this skill when:

- VS Code shows an error or warning in the Problems panel.
- A file has a red or yellow squiggle and the user wants it fixed.
- The language server reports a type, import, syntax, nullability, lint, build, or configuration diagnostic.
- A diagnostic appears after changing Java, TypeScript/React, Flutter/Dart, or project configuration.

Do not use this skill for a broad code review, a full dependency upgrade, or an unrelated runtime-only failure unless a Problems diagnostic is part of the evidence.

## Required Inputs

Capture these details before changing code when they are available:

- Exact diagnostic text, severity, source, and diagnostic code.
- Workspace-relative file path and 1-based line or symbol location.
- Language, module, selected interpreter/JDK, and relevant project configuration.
- User intent and expected behavior.
- Whether the diagnostic is new, stale, or reproducible after reload/build.

If the exact message is missing, inspect the Problems diagnostics and the owning file or symbol first. Ask only for the missing detail that prevents a safe diagnosis.

## Procedure

1. **Anchor on the diagnostic.** Locate the reported file, line, symbol, and nearby implementation. Read only enough surrounding code to identify the expression or declaration that produces the diagnostic.
2. **Classify the source.** Determine whether the diagnostic comes from the language server, compiler, linter, formatter, test adapter, or project configuration. An error blocks correctness or compilation; a warning may indicate a defect, an unsafe assumption, or an intentional pattern.
3. **Form one local hypothesis.** State what construct is triggering the diagnostic and why. Identify one cheap check that could disconfirm it, such as inspecting a type definition, checking an import, or reproducing the narrow command for that module.
4. **Check the nearest contract.** Inspect the symbol definition, caller, type/model, configuration, or generated source that controls the reported behavior. Prefer existing repository patterns and source-of-truth files.
5. **Choose the smallest valid fix.** Preserve public APIs and behavior unless the diagnostic reveals a real contract defect. Do not silence a diagnostic with a blanket suppression, ignore rule, unsafe cast, unused disable comment, or generated-file edit when the source of truth can be fixed.
6. **Edit the owning source.** For generated code, change the contract, generator input, or configuration and regenerate. Respect the project's layering, validation, generated-file, and public API conventions.
7. **Run focused validation immediately.** Prefer, in order:
   - the narrow test covering the affected behavior;
   - the module typecheck, lint, compile, or language-specific analyzer;
   - the smallest build command that reproduces the diagnostic;
   - a diagnostics refresh/reload if the issue may be stale.
8. **Recheck Problems.** Confirm the original diagnostic is gone, the fix did not introduce new diagnostics in the touched file/module, and no suppression hides a remaining issue.
9. **Report the result.** Summarize the root cause, changed files, focused validation, and any unrelated pre-existing diagnostics. If the diagnostic remains, report the exact blocker and the next narrow check rather than claiming success.

## Decision Rules

### Stale or environment diagnostic

If the code and project build are valid but Problems still reports the issue:

1. Verify the selected JDK/interpreter and workspace root.
2. Check whether dependencies and generated sources are present.
3. Refresh the language server, reload the window, or restart the analyzer only after recording the diagnostic.
4. Re-run the narrow compiler/analyzer command to distinguish an IDE cache issue from a real code issue.

Do not alter source code solely to make a stale diagnostic disappear.

### Error versus warning

- Errors require a correctness or compilation fix unless the diagnostic is demonstrably stale.
- Warnings require a fix when they expose a likely bug, invalid assumption, resource leak, unreachable path, or deprecated API.
- For intentional warnings, prefer a narrowly scoped, documented suppression only when the repository already accepts that pattern and the reason is clear.

### Generated or external code

Do not patch generated output, dependency code, build output, coverage output, or IDE metadata. Find and edit the generator input, API contract, source dependency, or project configuration, then regenerate or rebuild using the repository's documented command.

### Multiple diagnostics

Start with the first diagnostic that blocks parsing, typing, or compilation. Fix it and refresh diagnostics before addressing follow-on errors, since one root error can produce many secondary reports.

### Security-sensitive diagnostics

Treat diagnostics involving secrets, unsafe deserialization, injection, authentication, authorization, or unvalidated input as correctness and security issues. Preserve the repository's security boundaries and validate the fix with a focused test.

## Validation Commands

Inspect the project's documented scripts and build files, then run the narrowest command for the affected module, such as a focused test, typecheck, linter, compiler, analyzer, or package build. Do not guess a package manager or toolchain when the workspace declares one.

Do not run a full stack rebuild when a focused compiler, analyzer, or test can falsify the hypothesis.

## Completion Criteria

The task is complete only when:

- The original diagnostic is identified by exact message and location.
- A root cause or stale-environment cause is stated.
- The smallest appropriate source/configuration change is applied, or a concrete blocker is documented.
- A focused executable validation has run after the edit.
- The original Problems diagnostic is absent or explicitly explained as stale/tooling-related.
- No unrelated files or generated artifacts were modified.
- Remaining warnings or errors are called out rather than silently ignored.
