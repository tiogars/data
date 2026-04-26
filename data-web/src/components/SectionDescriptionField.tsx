import TextField from "@mui/material/TextField";
import { useSectionFormControllers } from "./useSectionFormControllers";

interface SectionDescriptionFieldProps {
  disabled?: boolean;
}

const SectionDescriptionField = ({ disabled }: SectionDescriptionFieldProps) => {
  const { descriptionController } = useSectionFormControllers();
  const { field, fieldState } = descriptionController;
  return (
    <TextField
      {...field}
      label="Description"
      fullWidth
      margin="normal"
      disabled={disabled}
      error={!!fieldState.error}
      helperText={fieldState.error?.message}
    />
  );
};

export default SectionDescriptionField;
