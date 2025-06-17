import React, {useEffect, useState} from 'react';
import {LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer} from 'recharts';
import {Dropdown} from 'primereact/dropdown';
import dayjs from 'dayjs';

import type {MeteringPoint, ConsumptionDetails} from '../types/metering';
import {fetchMeteringPoints, fetchConsumptionData} from '../api/consumption';
import YearPicker from '../components/YearPicker';

const Dashboard: React.FC = () => {
    const [meteringPoints, setMeteringPoints] = useState<MeteringPoint[]>([]);
    const [selectedMeteringPointId, setSelectedMeteringPointId] = useState<string | null>(null);
    const [data, setData] = useState<ConsumptionDetails[]>([]);
    const [year, setYear] = useState(() => dayjs().year());

    useEffect(() => {
        const loadMeteringPoints = async () => {
            try {
                const points = await fetchMeteringPoints();
                setMeteringPoints(points);
                if (points.length > 0) {
                    setSelectedMeteringPointId(points[0].id);
                }
            } catch (error) {
                console.error(error);
            }
        };
        loadMeteringPoints();
    }, []);

    useEffect(() => {
        const loadData = async () => {
            if (!selectedMeteringPointId) {
                return;
            }
            try {
                const from = dayjs().year(year).startOf('year').toISOString();
                const until = dayjs().year(year).endOf('year').toISOString();
                const result = await fetchConsumptionData(selectedMeteringPointId, from, until);
                setData(result);
            } catch (error) {
                console.error(error);
            }
        };
        loadData();
    }, [selectedMeteringPointId, year]);

    const formattedData = data.map(item => ({
        ...item,
        monthLabel: dayjs(year).month(item.month - 1).format('MMM')
    }));

    return (
        <div className="container mx-auto p-4">
            <div className="m-3">
                <h2 className="text-2xl font-semibold mb-4">Dashboard</h2>

                <div className="grid mb-4">
                    <div className="flex items-center col-4 align-items-center">
                        <label htmlFor="meteringPointSelect"
                               className="mr-2 whitespace-nowrap font-medium"
                        >
                            Select Metering Point:
                        </label>
                        <Dropdown inputId="meteringPointSelect"
                                  value={selectedMeteringPointId}
                                  options={meteringPoints.map(mp => ({label: mp.address, value: mp.id}))}
                                  onChange={(e) => setSelectedMeteringPointId(e.value)}
                                  placeholder="Select a Metering Point"
                                  className="w-48"
                        />
                    </div>
                    <div className="col-4 flex justify-content-center">
                        <YearPicker year={year} onChange={setYear}/>
                    </div>
                    <div className="col-4"></div>
                </div>

                <ResponsiveContainer width="100%" height={400}>
                    <LineChart data={formattedData} margin={{top: 20, right: 30, left: 20, bottom: 5}}>
                        <CartesianGrid strokeDasharray="3 3"/>
                        <XAxis dataKey="monthLabel"/>
                        <YAxis yAxisId="left" label={{value: 'kWh', angle: -90, position: 'insideLeft', style: { fill: '#8884d8' }}}/>
                        <YAxis yAxisId="right"
                               orientation="right"
                               label={{value: 'Cost (eur)', angle: 90, position: 'insideRight', style: { fill: '#82ca9d' }}}
                        />
                        <Tooltip/>
                        <Legend/>
                        <Line yAxisId="left" type="monotone" dataKey="kwhAmount" stroke="#8884d8" name="kWh"/>
                        <Line yAxisId="right" type="monotone" dataKey="costEur" stroke="#82ca9d" name="Cost (eur)"/>
                    </LineChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
};

export default Dashboard;