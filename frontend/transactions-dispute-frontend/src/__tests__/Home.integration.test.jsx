import React from 'react'
// mock api module (Home imports it using an absolute path)
const apiModulePath = '/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/api/axios'

vi.mock('/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/api/axios', () => ({
  default: {
    get: vi.fn(),
  },
}))

import { render, screen, waitFor } from '@testing-library/react'
import { MemoryRouter } from 'react-router-dom'
import Home from '../pages/Home'
import { AuthProvider } from '../context/AuthContext'

describe('Home integration', () => {
  beforeEach(() => {
    localStorage.clear()
  })

  test('fetches and displays transactions', async () => {
    const api = (await import('/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/api/axios')).default
    const tx = {
      id: 100,
      merchantName: 'Bookstore',
      transactionDate: '2026-05-13',
      currency: 'R',
      amount: '200.00',
      maskedCardNumber: '**** **** **** 5555',
      disputed: false,
    }

    api.get.mockResolvedValueOnce({ data: { content: [tx], totalPages: 1 } })

    // Set token and user so AuthProvider populates user
    localStorage.setItem('token', 'fake-token')
    localStorage.setItem('user', JSON.stringify({ id: 1, firstName: 'Test', balance: 1000 }))

    render(
      <MemoryRouter initialEntries={["/home"]}>
        <Home />
      </MemoryRouter>
    )

    expect(screen.getByText(/Recent Transactions/i)).toBeInTheDocument()

    // wait for transaction to appear
      <AuthProvider>
        <MemoryRouter initialEntries={["/home"]}>
          <Home />
        </MemoryRouter>
      </AuthProvider>
  })
})



