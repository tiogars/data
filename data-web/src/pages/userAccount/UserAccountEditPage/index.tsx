import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import UserAccountForm, { type UserAccountFormValues } from '../../../components/UserAccountForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useGetUserAccountQuery, useUpdateUserAccountMutation } from '../../../services/userAccountApi';
import type { UserAccountEditPageProps } from './UserAccountEditPage.types';

const emptyValues: UserAccountFormValues = {
  username: '',
  password: '',
  role: 'USER',
  enabled: true,
};

export const UserAccountEditPage: FC<UserAccountEditPageProps> = ({ id }) => {
  const navigate = useNavigate();
  const { data, isLoading, error } = useGetUserAccountQuery({ id });
  const [updateUserAccount, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateUserAccountMutation();
  const methods = useForm<UserAccountFormValues>({ defaultValues: emptyValues });
  const { reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        username: data.username ?? '',
        password: '',
        role: (data.role === 'ADMIN' ? 'ADMIN' : 'USER'),
        enabled: data.enabled ?? true,
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: UserAccountFormValues) => {
    await updateUserAccount({
      id,
      userAccountUpdateForm: {
        id,
        ...values,
      },
    }).unwrap();

    navigate(`/user-account/${id}`);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement du compte utilisateur</div>;
  if (!data) return <div>Compte utilisateur introuvable</div>;

  return (
    <CrudFormPageShell
      methods={methods}
      title="Modifier un compte utilisateur"
      subtitle="Mettez a jour le compte, son role et son etat."
      submitLabel="Enregistrer"
      onSubmit={onSubmit}
      isSubmitting={isSaving}
      showSuccess={isSuccess}
      successMessage="Compte utilisateur modifie avec succes."
      showError={Boolean(saveError)}
      errorMessage="Erreur lors de la modification du compte utilisateur."
    >
      <UserAccountForm disabled={isSaving} passwordRequired={false} />
    </CrudFormPageShell>
  );
};
