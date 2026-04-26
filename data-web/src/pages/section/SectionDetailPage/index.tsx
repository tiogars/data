import type { FC } from "react";
import { useGetSectionByIdQuery } from "../../../services/sectionApi";
import type { SectionDetailPageProps } from "./SectionDetailPage.types";

export const SectionDetailPage: FC<SectionDetailPageProps> = ({ id }) => {
  const { data, isLoading, error } = useGetSectionByIdQuery({ id });

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement de la section</div>;
  if (!data) return <div>Section introuvable</div>;

  return (
    <div>
      <h1>Détail de la section</h1>
      <div><b>ID :</b> {data.id}</div>
      <div><b>Nom :</b> {data.name}</div>
      <div><b>Description :</b> {data.description}</div>
    </div>
  );
};
