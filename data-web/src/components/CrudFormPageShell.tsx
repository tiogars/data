import type { ReactNode } from 'react';
import type { FieldValues, UseFormReturn } from 'react-hook-form';
import { FormProvider } from 'react-hook-form';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';

type CrudFormPageShellProps<TFormValues extends FieldValues> = {
  methods: UseFormReturn<TFormValues>;
  title: string;
  subtitle: string;
  maxWidth?: number | string;
  submitLabel: string;
  onSubmit: (values: TFormValues) => Promise<void> | void;
  isSubmitting: boolean;
  showSuccess?: boolean;
  successMessage?: string;
  showError?: boolean;
  errorMessage?: string;
  children: ReactNode;
};

export const CrudFormPageShell = <TFormValues extends FieldValues>({
  methods,
  title,
  subtitle,
  maxWidth = 640,
  submitLabel,
  onSubmit,
  isSubmitting,
  showSuccess,
  successMessage,
  showError,
  errorMessage,
  children,
}: CrudFormPageShellProps<TFormValues>) => {
  const { handleSubmit } = methods;

  return (
    <Box sx={{ maxWidth, mx: 'auto', width: '100%' }}>
      <Paper sx={{ p: { xs: 2.5, md: 3 }, mt: 3 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" component="h1">
              {title}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              {subtitle}
            </Typography>
          </Box>
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)}>
              <Stack spacing={2.5}>
                {children}
                <Button type="submit" variant="contained" disabled={isSubmitting} fullWidth>
                  {submitLabel}
                </Button>
              </Stack>
            </form>
          </FormProvider>
          {showSuccess && successMessage && <Alert severity="success">{successMessage}</Alert>}
          {showError && errorMessage && <Alert severity="error">{errorMessage}</Alert>}
        </Stack>
      </Paper>
    </Box>
  );
};
