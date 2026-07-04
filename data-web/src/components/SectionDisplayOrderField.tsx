import TextField from "@mui/material/TextField";
import { useSectionFormControllers } from "./useSectionFormControllers";

interface SectionDisplayOrderFieldProps {
  disabled?: boolean;
}

const SectionDisplayOrderField = ({ disabled = false }: SectionDisplayOrderFieldProps) => {
  const { displayOrderController } = useSectionFormControllers();
  const { field, fieldState } = displayOrderController;

  return (
    <TextField
      {...field}
      label="Ordre d'affichage"
      type="number"
      fullWidth
      margin="normal"
      disabled={disabled}
      error={Boolean(fieldState.error)}
      helperText={fieldState.error?.message ?? "Les plus petits nombres apparaissent en premier."}
      slotProps={{ htmlInput: { min: 0 } }}
    />
  );
};

export default SectionDisplayOrderField;