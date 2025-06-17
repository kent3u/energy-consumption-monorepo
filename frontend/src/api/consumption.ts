import type {ConsumptionDetails, MeteringPoint} from "../types/metering.ts";

export const fetchMeteringPoints = async (): Promise<MeteringPoint[]> => {
    const response = await fetch('/api/metering-point');
    if (!response.ok) {
        throw new Error('Failed to fetch metering points');
    }
    return await response.json();
};

export const fetchConsumptionData = async (
    meteringPointId: string,
    from: string,
    until: string
): Promise<ConsumptionDetails[]> => {
    const response = await fetch(`/api/consumption?meteringPointId=${meteringPointId}&from=${from}&until=${until}`);
    if (!response.ok) {
        throw new Error('Failed to fetch consumption data');
    }
    const json: ConsumptionDetails[] = await response.json();
    return json.sort((a, b) => a.month - b.month);
};