import '@testing-library/jest-dom'

// Provide a basic window.matchMedia mock for components that use it
if (typeof window !== 'undefined' && !window.matchMedia) {
  window.matchMedia = () => ({
    matches: false,
    addListener: () => {},
    removeListener: () => {},
  })
}

// Ensure React is available globally for older transpilation/runtime cases
import React from 'react'
if (typeof globalThis.React === 'undefined') {
  globalThis.React = React
}


