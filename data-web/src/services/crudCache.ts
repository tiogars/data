export const LIST_TAG_ID = 'LIST';

type EntityWithId = { id?: string };

type ListResult = { items?: EntityWithId[] } | undefined;

type IdQueryArg = { id: string };

/**
 * Collecte les identifiants non vides d'une liste d'entites.
 */
export function collectIds(items: EntityWithId[] | undefined): string[] {
  return (items ?? []).flatMap((item) => (item.id ? [item.id] : []));
}

/**
 * Construit les tags d'une liste : le tag LIST plus un tag par entite.
 */
export function createEntityTags<TTag extends string>(tag: TTag, ids: string[]) {
  return [{ type: tag, id: LIST_TAG_ID }, ...ids.map((id) => ({ type: tag, id }))];
}

/**
 * Fabrique les configurations d'invalidation RTK Query standard d'un domaine CRUD.
 */
export function createCrudCacheConfig<TTag extends string>(tag: TTag) {
  const itemAndListTags = (_result: unknown, _error: unknown, queryArg: IdQueryArg) => [
    { type: tag, id: queryArg.id },
    { type: tag, id: LIST_TAG_ID },
  ];

  return {
    detail: {
      providesTags: (_result: unknown, _error: unknown, queryArg: IdQueryArg) => [
        { type: tag, id: queryArg.id },
      ],
    },
    list: {
      providesTags: (result: ListResult) => createEntityTags(tag, collectIds(result?.items)),
    },
    create: {
      invalidatesTags: [{ type: tag, id: LIST_TAG_ID }],
    },
    update: {
      invalidatesTags: itemAndListTags,
    },
    remove: {
      invalidatesTags: itemAndListTags,
    },
    removeAll: {
      invalidatesTags: [tag],
    },
    importAll: {
      invalidatesTags: [tag],
    },
  };
}
