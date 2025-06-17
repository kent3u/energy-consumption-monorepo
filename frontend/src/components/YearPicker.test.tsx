import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect } from 'vitest'
import { vi } from 'vitest'
import YearPicker from './YearPicker';

describe('YearPicker', () => {
    const currentYear = new Date().getFullYear();

    it('renders the current year passed as prop', () => {
        render(<YearPicker year={2022} onChange={() => {}} />);
        expect(screen.getByText('2022')).toBeInTheDocument();
    });

    it('calls onChange with year - 1 when left button is clicked', () => {
        const onChangeMock = vi.fn();
        render(<YearPicker year={2022} onChange={onChangeMock} />);
        const leftButton = screen.getByLabelText('Previous Year');

        fireEvent.click(leftButton);
        expect(onChangeMock).toHaveBeenCalledWith(2021);
    });

    it('calls onChange with year + 1 when right button is clicked', () => {
        const onChangeMock = vi.fn();
        render(<YearPicker year={2022} onChange={onChangeMock} />);
        const rightButton = screen.getByLabelText('Next Year');

        fireEvent.click(rightButton);
        expect(onChangeMock).toHaveBeenCalledWith(2023);
    });

    it('does not allow year to go beyond current year', () => {
        const onChangeMock = vi.fn();
        render(<YearPicker year={currentYear} onChange={onChangeMock} />);
        const rightButton = screen.getByLabelText('Next Year');

        expect(rightButton).toBeDisabled();

        fireEvent.click(rightButton);
        expect(onChangeMock).not.toHaveBeenCalled();
    });
});