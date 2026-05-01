import { useFormContext, useController } from "react-hook-form";

export function useSectionFormControllers() {
  const { control } = useFormContext();
  const nameController = useController({ name: "name", control, rules: { required: "Le nom est requis" } });
  const descriptionController = useController({ name: "description", control, rules: { required: "La description est requise" } });
  const parentIdController = useController({ name: "parentId", control });
  return { nameController, descriptionController, parentIdController };
}
