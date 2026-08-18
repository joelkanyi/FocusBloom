/**
 * Feature convention: a UI + presentation module (one per screen). Applies the
 * library and Compose conventions. Dependency wiring to :core:model,
 * :core:common and :core:designsystem is added once those modules exist; a
 * feature never depends on another feature or on a data module (enforced by the
 * root checkModuleGraph task).
 */
plugins {
    id("focusbloom.kmp.library")
    id("focusbloom.compose")
}
