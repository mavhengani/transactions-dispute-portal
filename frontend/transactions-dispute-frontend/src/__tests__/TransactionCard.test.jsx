import React from 'react'
import { render, screen } from '@testing-library/react'
import TransactionCard from '../components/TransactionCard'

describe('TransactionCard', () => {
  const baseTx = {
    id: 1,
    merchantName: 'Coffee Shop',
    transactionDate: '2026-05-13',
    currency: 'R',
    amount: '45.00',
    maskedCardNumber: '**** **** **** 1234',
    disputed: false,
  }

  test('renders transaction details and success status', () => {
    render(<TransactionCard tx={baseTx} />)

    expect(screen.getByText('Coffee Shop')).toBeInTheDocument()
    expect(screen.getByText('2026-05-13')).toBeInTheDocument()
    expect(screen.getByText(/R 45.00/)).toBeInTheDocument()
    expect(screen.getByText('**** **** **** 1234')).toBeInTheDocument()
    expect(screen.getByText('SUCCESS')).toBeInTheDocument()
  })

  test('shows DISPUTED when tx.disputed is true', () => {
    const disputedTx = { ...baseTx, disputed: true }
    render(<TransactionCard tx={disputedTx} />)

    expect(screen.getByText('DISPUTED')).toBeInTheDocument()
  })
})


