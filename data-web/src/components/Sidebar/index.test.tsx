import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { MemoryRouter, Route, Routes, useLocation } from "react-router-dom";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import Sidebar from "./index";

const { mockUseListMenuItemsQuery, mockUseOidcAuth } = vi.hoisted(() => ({
  mockUseListMenuItemsQuery: vi.fn(),
  mockUseOidcAuth: vi.fn(),
}));

vi.mock("../../auth/OidcAuthProvider", () => ({
  useOidcAuth: mockUseOidcAuth,
}));

vi.mock("../../services/menuItemApi", () => ({
  useListMenuItemsQuery: mockUseListMenuItemsQuery,
}));

vi.mock("../../features/menuItem/iconRegistry", () => ({
  renderMenuItemIcon: (icon?: string) => <span>{icon ?? "icon"}</span>,
}));

const menuItemsResponse = {
  items: [
    {
      id: "games",
      label: "Games",
      icon: "sports_esports",
      children: [
        {
          id: "brick",
          label: "Brick",
          path: "/brick/list",
          icon: "view_module",
          children: [],
        },
      ],
    },
    {
      id: "business",
      label: "Données métier",
      icon: "storage",
      children: [
        {
          id: "section",
          label: "Sections",
          path: "/section/list",
          icon: "dataset",
          children: [],
        },
      ],
    },
  ],
};

const createMatchMedia = (matches: boolean) =>
  vi.fn().mockImplementation((query: string) => ({
    matches,
    media: query,
    onchange: null,
    addListener: vi.fn(),
    removeListener: vi.fn(),
    addEventListener: vi.fn(),
    removeEventListener: vi.fn(),
    dispatchEvent: vi.fn(),
  }));

const LocationDisplay = () => {
  const location = useLocation();
  return <div data-testid="location-display">{location.pathname}</div>;
};

function renderSidebar({
  initialEntries = ["/section/list"],
  isMobile = false,
  onClose = vi.fn(),
}: {
  initialEntries?: string[];
  isMobile?: boolean;
  onClose?: () => void;
} = {}) {
  window.matchMedia = createMatchMedia(isMobile);

  return {
    onClose,
    ...render(
      <MemoryRouter initialEntries={initialEntries}>
        <Routes>
          <Route
            path="*"
            element={
              <>
                <Sidebar open onClose={onClose} />
                <LocationDisplay />
              </>
            }
          />
        </Routes>
      </MemoryRouter>,
    ),
  };
}

describe("Sidebar", () => {
  beforeEach(() => {
    mockUseOidcAuth.mockReturnValue({
      isAuthenticated: true,
    });
    mockUseListMenuItemsQuery.mockReturnValue({
      data: menuItemsResponse,
      isError: false,
      isLoading: false,
    });
    window.matchMedia = createMatchMedia(false);
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it("expands only the branch that matches the selected route", () => {
    renderSidebar({
      initialEntries: ["/section/list"],
    });

    expect(screen.getByRole("link", { name: /sections/i })).toBeInTheDocument();
    expect(screen.queryByRole("link", { name: /brick/i })).not.toBeInTheDocument();
  });

  it("closes the previously expanded branch when another group is opened", async () => {
    const user = userEvent.setup();

    renderSidebar({
      initialEntries: ["/section/list"],
    });

    await user.click(screen.getByRole("button", { name: /games/i }));

    expect(screen.getByRole("link", { name: /brick/i })).toBeInTheDocument();
    expect(screen.queryByRole("link", { name: /sections/i })).not.toBeInTheDocument();
  });

  it("closes the mobile drawer after selecting a menu item", async () => {
    const user = userEvent.setup();
    const onClose = vi.fn();

    renderSidebar({
      initialEntries: ["/brick/list"],
      isMobile: true,
      onClose,
    });

    await user.click(screen.getByRole("button", { name: /données métier/i }));
    await user.click(screen.getByRole("link", { name: /sections/i }));

    expect(screen.getByTestId("location-display")).toHaveTextContent("/section/list");
    expect(onClose).toHaveBeenCalledTimes(1);
  });
});
