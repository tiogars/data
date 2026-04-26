import { useForm } from "react-hook-form";
import { useCreateSectionMutation } from "../../../services/sectionApi";
import type { FC } from "react";
import type { SectionCreatePageProps } from "./SectionCreatePage.types";

export const SectionCreatePage: FC<SectionCreatePageProps> = () => {
  const [createSection, { isLoading, error, isSuccess }] = useCreateSectionMutation();
  const { register, handleSubmit, reset } = useForm({
    defaultValues: { name: "", description: "" },
  });

  const onSubmit = async (values: { name: string; description: string }) => {
    await createSection({ sectionCreationForm: values });
    reset();
  };

  return (
    <div>
      <h1>Créer une section</h1>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div>
          <label htmlFor="name">Nom</label>
          <input id="name" {...register("name", { required: true })} />
        </div>
        <div>
          <label htmlFor="description">Description</label>
          <input id="description" {...register("description", { required: true })} />
        </div>
        <button type="submit" disabled={isLoading}>Créer</button>
      </form>
      {isSuccess && <div>Section créée !</div>}
      {error && <div>Erreur lors de la création</div>}
    </div>
  );
};
