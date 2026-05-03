import { useEffect, useMemo, useState } from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import CircularProgress from '@mui/material/CircularProgress';
import Divider from '@mui/material/Divider';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import { useOidcAuth } from '../../../auth/OidcAuthProvider';
import {
  type JpaEntityAttributeInfo,
  type JpaEntityClassInfo,
  useGetJpaEntitiesInfoQuery,
} from '../../../services/serverInfoApi';

function formatBoolean(value: boolean | undefined): string {
  if (value === undefined) return '-';
  return value ? 'oui' : 'non';
}

function formatColumnInfo(attribute: JpaEntityAttributeInfo): string {
  if (!attribute.column) return '-';

  const { column } = attribute;
  return [
    `name: ${column.name ?? '-'}`,
    `nullable: ${formatBoolean(column.nullable)}`,
    `updatable: ${formatBoolean(column.updatable)}`,
    `insertable: ${formatBoolean(column.insertable)}`,
    `unique: ${formatBoolean(column.unique)}`,
    `length: ${column.length ?? '-'}`,
    `precision: ${column.precision ?? '-'}`,
    `scale: ${column.scale ?? '-'}`,
    `definition: ${column.columnDefinition ?? '-'}`,
  ].join(' | ');
}

function formatRelationInfo(attribute: JpaEntityAttributeInfo): string {
  const parts: string[] = [];

  if (attribute.manyToOne) {
    parts.push(`@ManyToOne(fetch=${attribute.manyToOne.fetch ?? '-'}, optional=${formatBoolean(attribute.manyToOne.optional)})`);

    if ((attribute.manyToOne.cascade?.length ?? 0) > 0) {
      parts.push(`cascade=${attribute.manyToOne.cascade?.join(',')}`);
    }
  }

  if (attribute.joinColumn) {
    parts.push(
      [
        '@JoinColumn(',
        `name=${attribute.joinColumn.name ?? '-'}`,
        `referencedColumnName=${attribute.joinColumn.referencedColumnName ?? '-'}`,
        `nullable=${formatBoolean(attribute.joinColumn.nullable)}`,
        `updatable=${formatBoolean(attribute.joinColumn.updatable)}`,
        `insertable=${formatBoolean(attribute.joinColumn.insertable)}`,
        `unique=${formatBoolean(attribute.joinColumn.unique)}`,
        ')',
      ].join(' '),
    );
  }

  if (parts.length === 0) return '-';

  return parts.join(' | ');
}

function AttributeFlags({ attribute }: Readonly<{ attribute: JpaEntityAttributeInfo }>) {
  const generatedValueLabel = attribute.generationStrategy
    ? `@GeneratedValue(${attribute.generationStrategy})`
    : '@GeneratedValue';

  return (
    <Stack direction="row" spacing={0.75} useFlexGap sx={{ flexWrap: 'wrap' }}>
      {attribute.id && <Chip label="@Id" size="small" color="primary" variant="outlined" />}
      {attribute.generated && (
        <Chip
          label={generatedValueLabel}
          size="small"
          color="secondary"
          variant="outlined"
        />
      )}
      {attribute.version && <Chip label="@Version" size="small" variant="outlined" />}
      {attribute.lob && <Chip label="@Lob" size="small" variant="outlined" />}
      {attribute.transientField && <Chip label="@Transient" size="small" variant="outlined" />}
      {attribute.manyToOne && <Chip label="@ManyToOne" size="small" variant="outlined" />}
      {attribute.joinColumn && <Chip label="@JoinColumn" size="small" variant="outlined" />}
      {attribute.column && <Chip label="@Column" size="small" variant="outlined" />}
    </Stack>
  );
}

function EntityDetailHeader({ entity }: Readonly<{ entity: JpaEntityClassInfo }>) {
  return (
    <Stack spacing={1.5}>
      <Typography variant="h5" component="h2">
        {entity.simpleClassName ?? 'Entite inconnue'}
      </Typography>
      <Typography variant="body2" color="text.secondary">
        Classe: {entity.className ?? '-'}
      </Typography>
      <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: 'wrap' }}>
        <Chip label={`@Table(name=${entity.tableName ?? '-'})`} color="primary" variant="outlined" />
        {entity.entityName && <Chip label={`@Entity(name=${entity.entityName})`} variant="outlined" />}
        {entity.tableSchema && <Chip label={`schema=${entity.tableSchema}`} variant="outlined" />}
        {entity.tableCatalog && <Chip label={`catalog=${entity.tableCatalog}`} variant="outlined" />}
      </Stack>
    </Stack>
  );
}

function EntityAttributesDesktop({ attributes }: Readonly<{ attributes: JpaEntityAttributeInfo[] }>) {
  return (
    <TableContainer component={Paper} variant="outlined">
      <Table size="small" aria-label="attributs entite jpa">
        <TableHead>
          <TableRow>
            <TableCell sx={{ fontWeight: 700 }}>Nom</TableCell>
            <TableCell sx={{ fontWeight: 700 }}>Type</TableCell>
            <TableCell sx={{ fontWeight: 700 }}>Flags</TableCell>
            <TableCell sx={{ fontWeight: 700 }}>@Column</TableCell>
            <TableCell sx={{ fontWeight: 700 }}>Relations</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {attributes.map((attribute) => (
            <TableRow key={attribute.name}>
              <TableCell>{attribute.name ?? '-'}</TableCell>
              <TableCell>{attribute.type ?? '-'}</TableCell>
              <TableCell sx={{ minWidth: 260 }}>
                <AttributeFlags attribute={attribute} />
              </TableCell>
              <TableCell sx={{ minWidth: 280 }}>
                <Typography variant="caption">{formatColumnInfo(attribute)}</Typography>
              </TableCell>
              <TableCell sx={{ minWidth: 320 }}>
                <Typography variant="caption">{formatRelationInfo(attribute)}</Typography>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}

function EntityAttributesMobile({ attributes }: Readonly<{ attributes: JpaEntityAttributeInfo[] }>) {
  return (
    <Stack spacing={1.2}>
      {attributes.map((attribute) => (
        <Paper key={attribute.name} variant="outlined" sx={{ p: 1.5 }}>
          <Stack spacing={1}>
            <Typography variant="subtitle2">
              {attribute.name ?? '-'}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Type: {attribute.type ?? '-'}
            </Typography>
            <AttributeFlags attribute={attribute} />
            <Typography variant="caption">@Column: {formatColumnInfo(attribute)}</Typography>
            <Typography variant="caption">Relations: {formatRelationInfo(attribute)}</Typography>
          </Stack>
        </Paper>
      ))}
    </Stack>
  );
}

export const JpaEntitiesPage = () => {
  const theme = useTheme();
  const isDesktop = useMediaQuery(theme.breakpoints.up('lg'));
  const { isAuthenticated, isLoading: isAuthLoading, login } = useOidcAuth();
  const { data, isLoading, isFetching, isError, refetch } = useGetJpaEntitiesInfoQuery(undefined, {
    skip: isAuthLoading || !isAuthenticated,
  });

  const entities = useMemo(
    () => [...(data?.items ?? [])].sort((a, b) => (a.simpleClassName ?? '').localeCompare(b.simpleClassName ?? '')),
    [data?.items],
  );

  const [selectedClassName, setSelectedClassName] = useState<string | null>(null);

  useEffect(() => {
    if (entities.length === 0) {
      setSelectedClassName(null);
      return;
    }

    setSelectedClassName((current) => {
      const hasCurrent = current && entities.some((entity) => entity.className === current);
      if (hasCurrent) return current;
      return entities[0].className ?? null;
    });
  }, [entities]);

  const selectedEntity = useMemo(
    () => entities.find((entity) => entity.className === selectedClassName) ?? entities[0] ?? null,
    [entities, selectedClassName],
  );

  const selectedAttributes = selectedEntity?.attributes ?? [];

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Box>
        <Typography variant="h4" component="h1">
          Entites JPA du serveur
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Classes annotees @Entity et details des annotations jakarta.persistence sur leurs attributs.
        </Typography>
      </Box>

      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.2}>
        <Button variant="outlined" onClick={() => refetch()} disabled={isFetching}>
          Rafraichir
        </Button>
        {!isAuthenticated && (
          <Button variant="contained" onClick={() => login()}>
            Se connecter
          </Button>
        )}
      </Stack>

      {!isAuthLoading && !isAuthenticated && (
        <Alert severity="warning">
          Vous devez etre connecte pour consulter les metadonnees des entites JPA.
        </Alert>
      )}

      {isLoading && (
        <Paper variant="outlined" sx={{ p: 3 }}>
          <Stack direction="row" spacing={1.2} sx={{ alignItems: 'center' }}>
            <CircularProgress size={20} />
            <Typography>Chargement des entites JPA...</Typography>
          </Stack>
        </Paper>
      )}

      {isError && (
        <Alert severity="error">
          Impossible de recuperer la liste des entites JPA. Verifiez que le backend est demarre.
        </Alert>
      )}

      {!isLoading && !isError && entities.length === 0 && (
        <Alert severity="info">Aucune classe annotee @Entity n'a ete detectee.</Alert>
      )}

      {!isLoading && !isError && entities.length > 0 && (
        <Box
          sx={{
            display: 'grid',
            gap: 2,
            gridTemplateColumns: { xs: '1fr', lg: '320px minmax(0, 1fr)' },
            alignItems: 'start',
          }}
        >
          <Paper variant="outlined" sx={{ p: 2, minHeight: 420 }}>
            <Stack spacing={1.5}>
              <Stack direction="row" spacing={1} sx={{ alignItems: 'center', justifyContent: 'space-between' }}>
                <Typography variant="h6">Entites</Typography>
                <Chip label={`${entities.length}`} size="small" color="primary" variant="outlined" />
              </Stack>
              <Typography variant="body2" color="text.secondary">
                Selectionnez une entite pour afficher le detail des attributs et annotations.
              </Typography>
              <Divider />
              <List dense disablePadding>
                {entities.map((entity) => (
                  <ListItem key={entity.className} disablePadding>
                    <ListItemButton
                      selected={entity.className === selectedEntity?.className}
                      onClick={() => setSelectedClassName(entity.className ?? null)}
                    >
                      <ListItemText
                        primary={entity.simpleClassName ?? entity.className}
                        secondary={entity.tableName ? `table: ${entity.tableName}` : 'table: par defaut'}
                      />
                    </ListItemButton>
                  </ListItem>
                ))}
              </List>
            </Stack>
          </Paper>

          <Paper variant="outlined" sx={{ p: { xs: 1.5, md: 2.5 }, minHeight: 420 }}>
            {selectedEntity ? (
              <Stack spacing={2}>
                <EntityDetailHeader entity={selectedEntity} />
                <Divider />
                <Typography variant="h6">Attributs ({selectedAttributes.length})</Typography>
                {isDesktop ? (
                  <EntityAttributesDesktop attributes={selectedAttributes} />
                ) : (
                  <EntityAttributesMobile attributes={selectedAttributes} />
                )}
              </Stack>
            ) : (
              <Typography color="text.secondary">Selectionnez une entite pour voir ses details.</Typography>
            )}
          </Paper>
        </Box>
      )}
    </Stack>
  );
};