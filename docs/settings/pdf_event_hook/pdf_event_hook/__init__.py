# pdf_event_hook/__init__.py
# https://mkdocs-to-pdf.readthedocs.io/en/latest/

import logging

from bs4 import BeautifulSoup
from mkdocs.structure.pages import Page


def inject_link(html: str, href: str,
                page: Page, logger: logging) -> str:
    """Adding PDF View button on navigation bar (using material theme)"""

    logger.info('################################################################################')
    logger.info('Injecting PDF link into page navigation bar')

    def _pdf_icon():
        _ICON = '''
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
<path d="M128,0c-17.6,0-32,14.4-32,32v448c0,17.6,14.4,32,32,32h320c17.6,0,32-14.4,32-32V128L352,0H128z" fill="#E2E5E7"/>
<path d="m384 128h96l-128-128v96c0 17.6 14.4 32 32 32z" fill="#B0B7BD"/>
<polygon points="480 224 384 128 480 128" fill="#CAD1D8"/>
<path d="M416,416c0,8.8-7.2,16-16,16H48c-8.8,0-16-7.2-16-16V256c0-8.8,7.2-16,16-16h352c8.8,0,16,7.2,16,16  V416z" fill="#F15642"/>
<g fill="#fff">
<path d="m101.74 303.15c0-4.224 3.328-8.832 8.688-8.832h29.552c16.64 0 31.616 11.136 31.616 32.48 0 20.224-14.976 31.488-31.616 31.488h-21.36v16.896c0 5.632-3.584 8.816-8.192 8.816-4.224 0-8.688-3.184-8.688-8.816v-72.032zm16.88 7.28v31.872h21.36c8.576 0 15.36-7.568 15.36-15.504 0-8.944-6.784-16.368-15.36-16.368h-21.36z"/>
<path d="m196.66 384c-4.224 0-8.832-2.304-8.832-7.92v-72.672c0-4.592 4.608-7.936 8.832-7.936h29.296c58.464 0 57.184 88.528 1.152 88.528h-30.448zm8.064-72.912v57.312h21.232c34.544 0 36.08-57.312 0-57.312h-21.232z"/>
<path d="m303.87 312.11v20.336h32.624c4.608 0 9.216 4.608 9.216 9.072 0 4.224-4.608 7.68-9.216 7.68h-32.624v26.864c0 4.48-3.184 7.92-7.664 7.92-5.632 0-9.072-3.44-9.072-7.92v-72.672c0-4.592 3.456-7.936 9.072-7.936h44.912c5.632 0 8.96 3.344 8.96 7.936 0 4.096-3.328 8.704-8.96 8.704h-37.248v0.016z"/>
</g>
<path d="m400 432h-304v16h304c8.8 0 16-7.2 16-16v-16c0 8.8-7.2 16-16 16z" fill="#CAD1D8"/>
</svg>
'''  # noqa: E501
        icon_soup = BeautifulSoup(_ICON, 'html.parser')
        return icon_soup.svg

    logger.info(f'(hook on inject_link: {page.title}, href: {href})')

    # Determine if the current page url is starting with the French version
    logger.info(f'Page URL: {page.url}')
    if 'fr/' in page.url:
        href = 'fr/' + href
    soup = BeautifulSoup(html, 'html.parser')

# md-header__inner md-grid

    nav = soup.find(class_='md-header-nav')
    if not nav:
        # after 7.x
        nav = soup.find('nav', class_='md-header__inner md-grid')
    
    if nav:
        logger.info(f'Found nav element: {nav.name}, classes: {nav.get("class")}')

        logger.info('Creating PDF link element')
        a = soup.new_tag('a', href=href, title='PDF',
                         **{'class': 'md-header__button md-icon'})
        icon = _pdf_icon()
        #logger.info(f'PDF icon element: {icon}')
        a.append(icon)

        nav.append(a)
        #logger.info(f'Result element: {nav}')

        logger.info('PDF link injected successfully')
        # Build final HTML string and also update page.content so MkDocs/with-pdf
        # can pick up the modified version if it relies on the Page object.
        try:
            modified_html = str(soup)
            # Some versions of MkDocs Page store main body in page.content;
            # updating it increases chance the injected link is preserved.
            page.content = modified_html  # type: ignore[attr-defined]
            logger.debug('page.content updated with modified HTML')
        except Exception as e:  # pragma: no cover - defensive
            logger.warning(f'Could not update page.content: {e}')
        finally:
            logger.info('################################################################################')

        return modified_html
    else:
        logger.warning('Nav element not found in HTML')

    return html


# def pre_js_render(soup: BeautifulSoup, logger: logging) -> BeautifulSoup:
#     logger.info('(hook on pre_js_render)')
#     return soup


# def pre_pdf_render(soup: BeautifulSoup, logger: logging) -> BeautifulSoup:
#     logger.info('(hook on pre_pdf_render)')
#     tag = soup.find(lambda tag: tag.name ==
#                     'body' and 'data-md-color-scheme' in tag.attrs)
#     if tag:
#         tag['data-md-color-scheme'] = 'print'
#     return soup