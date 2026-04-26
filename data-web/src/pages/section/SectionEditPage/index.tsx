import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { useGetSectionByIdQuery, useUpdateSectionMutation } from "../../../services/sectionApi";
import type { FC } from "react";
import type { SectionEditPageProps } from "./SectionEditPage.types";

export const SectionEditPage: FC<SectionEditPageProps> = ({ id }) => {
  const { data, isLoading, error } = useGetSectionByIdQuery({ id });
  const [updateSection, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateSectionMutation();
  const { register, handleSubmit, reset } = useForm({
    defaultValues: { name: "", description: "" },
  });

  useEffect(() => {
    if (data) {
      reset({ name: data.name ?? "", description: data.description ?? "" });
    }
  }, [data, reset]);

  const onSubmit = async (values: { name: string; description: string }) => {
    await updateSection({ id, section: { id, ...values } });
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement de la section</div>;
  if (!data) return <div>Section introuvable</div>;

  return (
    <div>
      <h1>Modifier la section</h1>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div>
          <label htmlFor="name">Nom</label>
          <input id="name" {...register("name", { required: true })} />
        </div>
        <div>
          <label htmlFor="description">Description</label>
          <input id="description" {...register("description", { required: true })} />
        </div>
        <button type="submit" disabled={isSaving}>Enregistrer</button>
      </form>
      {isSuccess && <div>Section modifiée !</div>}
      {saveError && <div>Erreur lors de la modification</div>}
    </div>
  );
};
