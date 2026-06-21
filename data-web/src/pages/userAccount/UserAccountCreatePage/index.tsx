import { useForm } from 'react-hook-form';
import type { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import UserAccountForm, { type UserAccountFormValues } from '../../../components/UserAccountForm';
import { CrudFormPageShell } from '../../../components/CrudFormPageShell';
import { useCreateUserAccountMutation } from '../../../services/userAccountApi';
import type { UserAccountCreatePageProps } from './UserAccountCreatePage.types';

const defaultValues: UserAccountFormValues = {
  username: '',
  password: '',
  role: 'USER',
  enabled: true,
};

export const UserAccountCreatePage: FC<UserAccountCreatePageProps> = ({ onCreated }) => {
  const navigate = useNavigate();
  const [createUserAccount, { isLoading, error, isSuccess }] = useCreateUserAccountMutation();
  const methods = useForm<UserAccountFormValues>({ defaultValues });
  const { reset } = methods;

  const onSubmit = async (values: UserAccountFormValues) => {
    const createdUser = await createUserAccount({
      userAccountCreationForm: values,
    }).unwrap();

    reset(defaultValues);

    if (createdUser.id) {
      await onCreated?.(createdUser.id);
      navigate(`/user-account/${createdUser.id}`);
    }
  };

  return (
    <CrudFormPageShell
      methods={methods}
      title="Creer un compte utilisateur"
      subtitle="Ajoutez un utilisateur interne avec son role et son mot de passe."
      submitLabel="Creer"
      onSubmit={onSubmit}
      isSubmitting={isLoading}
      showSuccess={isSuccess}
      successMessage="Compte utilisateur cree avec succes."
      showError={Boolean(error)}
      errorMessage="Erreur lors de la creation du compte utilisateur."
    >
      <UserAccountForm disabled={isLoading} />
    </CrudFormPageShell>
  );
};
