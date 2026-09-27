## Related story

- HU: `HU-XXX`
- Source branch: `hu-xxx-dev` or `hu-xxx-qa`
- Target branch: `Develop` or `Qa`; use `main` only for the completed MVP release

## Summary

Describe the value delivered and the reason for the change.

## Scope

- Included:
- Not included:

## Acceptance criteria

- [ ] Criterion 1
- [ ] Criterion 2

## Technical changes

- Modules affected:
- API or contract changes:
- Database changes:
- Configuration or environment changes:

## Validation

Commands executed:

```text
Add commands here
```

Results:

```text
Add results here
```

## Risks and rollback

- Known risks:
- Rollback approach:

## Checklist

- [ ] The change satisfies every acceptance criterion.
- [ ] Automated tests pass locally.
- [ ] CI passes for this Pull Request.
- [ ] OpenAPI and Pact were updated when a contract changed.
- [ ] Documentation and QA evidence were updated when applicable.
- [ ] No secrets, environment files, logs, or generated artifacts are included.
- [ ] A story PR follows `hu-dev -> Develop` or `hu-qa -> Qa`.
- [ ] A `Qa -> main` PR is used only when the complete MVP is approved.
