import type { Meta, StoryObj } from '@storybook/react-vite';

import Header from './index';

const meta: Meta<typeof Header> = {
    title: 'Components/Header',
    component: Header,
};

export default meta;
type Story = StoryObj<typeof Header>;

export const Default: Story = {};