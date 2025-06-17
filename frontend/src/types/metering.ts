export type ConsumptionDetails = {
    month: number;
    kwhAmount: number;
    costEur: number;
};

export type MeteringPoint = {
    id: string;
    address: string;
};