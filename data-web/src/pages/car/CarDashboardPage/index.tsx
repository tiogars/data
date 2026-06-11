import { useEffect, useMemo, useState, type FC } from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import MenuItem from '@mui/material/MenuItem';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { LineChart } from '@mui/x-charts/LineChart';
import { useListCarsQuery } from '../../../services/carApi';
import { useChartCarMileagesQuery } from '../../../services/carMileageApi';
import type { CarDashboardPageProps } from './CarDashboardPage.types';

export const CarDashboardPage: FC<CarDashboardPageProps> = () => {
  const { data: carsData, isLoading: carsLoading, error: carsError } = useListCarsQuery();
  const cars = useMemo(
    () => (carsData?.items ?? []).filter((car): car is { id: string; name: string } => Boolean(car.id && car.name)),
    [carsData?.items],
  );

  const [selectedCarId, setSelectedCarId] = useState('');

  useEffect(() => {
    if (!selectedCarId && cars.length > 0) {
      setSelectedCarId(cars[0].id);
    }
  }, [cars, selectedCarId]);

  const { data: chartData, isLoading: chartLoading, error: chartError } = useChartCarMileagesQuery(
    { carId: selectedCarId },
    { skip: !selectedCarId },
  );

  const chartPoints = chartData?.points ?? [];
  const xValues = chartPoints.map((point) => (point.readingAt ? new Date(point.readingAt) : new Date()));
  const yValues = chartPoints.map((point) => point.odometerKm ?? 0);

  if (carsLoading) return <div>Chargement...</div>;
  if (carsError) return <div>Erreur lors du chargement des voitures</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Box>
        <Typography variant="h4" component="h1">
          Dashboard kilometrage
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Visualisez l'evolution du kilometrage pour une voiture selectionnee.
        </Typography>
      </Box>

      {cars.length === 0 && <Alert severity="info">Ajoutez d'abord une voiture dans la page "Ensemble de voitures".</Alert>}

      {cars.length > 0 && (
        <TextField
          select
          fullWidth
          label="Voiture"
          value={selectedCarId}
          onChange={(event) => setSelectedCarId(event.target.value)}
        >
          {cars.map((car) => (
            <MenuItem key={car.id} value={car.id}>
              {car.name}
            </MenuItem>
          ))}
        </TextField>
      )}

      <Paper variant="outlined" sx={{ p: { xs: 2, md: 3 } }}>
        <Typography variant="h6" sx={{ mb: 1.5 }}>
          Evolution du kilometrage
        </Typography>

        {chartLoading && <Typography>Chargement du graphique...</Typography>}
        {chartError && <Alert severity="error">Erreur lors du chargement des releves.</Alert>}
        {!chartLoading && !chartError && selectedCarId && chartPoints.length === 0 && (
          <Alert severity="info">Aucun releve pour cette voiture.</Alert>
        )}
        {!chartLoading && !chartError && chartPoints.length > 0 && (
          <LineChart
            height={360}
            xAxis={[
              {
                data: xValues,
                scaleType: 'time',
                valueFormatter: (value) => value.toLocaleDateString('fr-FR'),
              },
            ]}
            series={[{ data: yValues, label: 'Kilometrage (km)', curve: 'linear' }]}
            margin={{ top: 24, right: 24, bottom: 24, left: 56 }}
          />
        )}
      </Paper>
    </Stack>
  );
};
