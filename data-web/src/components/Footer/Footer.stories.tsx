import type { Meta, StoryObj } from '@storybook/react-vite';

import Footer from './index';
import type { FooterLink } from '../../services/footerLinkApi';

const storyItems: FooterLink[] = [
    { id: '1', label: 'React', url: 'https://react.dev/', icon: 'react', displayOrder: 10 },
    { id: '2', label: 'MUI', url: 'https://mui.com/', icon: 'mui', displayOrder: 20 },
    { id: '3', label: 'GitHub', url: 'https://github.com/tiogars/data', icon: 'github', displayOrder: 30 },
];

const meta: Meta<typeof Footer> = {
    title: 'Components/Footer',
    component: Footer,
};

export default meta;
type Story = StoryObj<typeof Footer>;

export const Default: Story = {
    args: {
        items: storyItems,
    },
};