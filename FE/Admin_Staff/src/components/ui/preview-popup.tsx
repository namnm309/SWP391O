"use client"

import React from "react"

interface PreviewPopupProps {
  isOpen: boolean
  onClose: () => void
  children: React.ReactNode
}

export function PreviewPopup({ isOpen, onClose, children }: PreviewPopupProps) {
  React.useEffect(() => {
    const handleEscape = (e: KeyboardEvent) => {
      if (e.key === "Escape") onClose()
    }

    window.addEventListener("keydown", handleEscape)
    return () => window.removeEventListener("keydown", handleEscape)
  }, [onClose])

  const handleBackdropClick = (e: React.MouseEvent) => {
    if (e.target === e.currentTarget) onClose()
  }

  if (!isOpen) return null

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4"
      onClick={handleBackdropClick}
    >
      <div>
        {children}
      </div>
    </div>
  )
}
