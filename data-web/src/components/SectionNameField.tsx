import TextField from "@mui/material/TextField";
import { useSectionFormControllers } from "./useSectionFormControllers";

interface SectionNameFieldProps {
  disabled?: boolean;
}

const SectionNameField = ({ disabled }: SectionNameFieldProps) => {
  const { nameController } = useSectionFormControllers();
  const { field, fieldState } = nameController;
  return (
    <TextField
      {...field}
      label="Nom"
      fullWidth
      margin="normal"
      disabled={disabled}
      error={!!fieldState.error}
      helperText={fieldState.error?.message}
    />
  );
};

export default SectionNameField;
