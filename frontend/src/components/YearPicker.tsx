import React from 'react';
import {Button} from "primereact/button";

type YearPickerProps = {
    year: number;
    onChange: (year: number) => void;
};

const YearPicker: React.FC<YearPickerProps> = ({ year, onChange }) => {
    const currentYear = new Date().getFullYear();

    return (
        <div className="flex items-center align-items-center gap-2">
            <Button
                icon="pi pi-chevron-left"
                className="p-button-text"
                onClick={() => onChange(year - 1)}
                aria-label="Previous Year"
            />
            <span className="text-lg font-semibold w-16 text-center">{year}</span>
            <Button
                icon="pi pi-chevron-right"
                className="p-button-text"
                onClick={() => onChange(Math.min(currentYear, year + 1))}
                disabled={year >= currentYear}
                aria-label="Next Year"
            />
        </div>
    );
};

export default YearPicker;
