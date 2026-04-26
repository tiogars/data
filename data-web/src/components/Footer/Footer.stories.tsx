import type { Meta, StoryObj } from '@storybook/react-vite';

import Footer from './index';

const meta: Meta<typeof Footer> = {
    title: 'Components/Footer',
    component: Footer,
};

export default meta;
type Story = StoryObj<typeof Footer>;

export const Default: Story = {};