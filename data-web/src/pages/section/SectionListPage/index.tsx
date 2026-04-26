import type { FC } from "react";
import { useListSectionsQuery } from "../../../services/sectionApi";
import type { SectionListPageProps } from "./SectionListPage.types";

export const SectionListPage: FC<SectionListPageProps> = () => {
  const { data, isLoading, error } = useListSectionsQuery();

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des sections</div>;

  return (
    <div>
      <h1>Liste des sections</h1>
      <ul>
        {data?.items?.map((section) => (
          <li key={section.id}>
            {section.name} - {section.description}
          </li>
        ))}
      </ul>
      <div>Total : {data?.count ?? 0}</div>
    </div>
  );
};
