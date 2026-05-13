import React from 'react'
// The component imports api using an absolute path in the source file.
const apiModulePath = '/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/api/axios'

vi.mock('/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/api/axios', () => ({
  default: {
    put: vi.fn(),
  },
}))

import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import TransactionModal from '../components/TransactionModal'

describe('TransactionModal', () => {
  const tx = {
    id: 42,
    merchantName: 'Grocery',
    currency: 'R',
    amount: '120.00',
    transactionType: 'SALE',
    status: 'SUCCESS',
    referenceNumber: 'REF-123',
    paymentMethod: 'CARD',
    location: 'Cape Town',
    maskedCardNumber: '**** **** **** 4321',
    transactionDate: '2026-05-13',
    disputed: false,
  }

  afterEach(() => {
    vi.clearAllMocks()
  })

  test('calls API and close/refresh when marking disputed', async () => {
    const api = (await import('/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/api/axios')).default
    api.put.mockResolvedValueOnce({})

    const close = vi.fn()
    const refresh = vi.fn()
    const user = userEvent.setup()

    // spy on global alert
    const alertSpy = vi.spyOn(global, 'alert').mockImplementation(() => {})

    render(<TransactionModal tx={tx} close={close} refresh={refresh} />)

    const btn = screen.getByRole('button', { name: /Mark As Disputed/i })
    await user.click(btn)

    expect(api.put).toHaveBeenCalledWith(`/transactions/${tx.id}/dispute`)
    expect(alertSpy).toHaveBeenCalledWith('Transaction disputed')
    expect(refresh).toHaveBeenCalled()
    expect(close).toHaveBeenCalled()

    alertSpy.mockRestore()
  })

  test('shows error alert when API fails', async () => {
    const api = (await import('/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/api/axios')).default
    const error = { response: { data: 'boom' } }
    api.put.mockRejectedValueOnce(error)

    const close = vi.fn()
    const refresh = vi.fn()
    const user = userEvent.setup()

    const alertSpy = vi.spyOn(global, 'alert').mockImplementation(() => {})

    render(<TransactionModal tx={tx} close={close} refresh={refresh} />)

    const btn = screen.getByRole('button', { name: /Mark As Disputed/i })
    await user.click(btn)

    // expect alert with the API error message
    expect(alertSpy).toHaveBeenCalledWith('boom')
    expect(refresh).not.toHaveBeenCalled()
    expect(close).not.toHaveBeenCalled()

    alertSpy.mockRestore()
  })
})



