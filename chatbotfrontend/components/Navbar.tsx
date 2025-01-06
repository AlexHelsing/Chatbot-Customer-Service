'use client';

import React, { useState, useEffect } from 'react';

const Navbar: React.FC = () => {
    const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);

    const navItems = [
        { id: 1, text: 'Home', href: '/' },
        { id: 2, text: 'Contact', href: '#Kontakt'},
    ];


    const toggleDropdown = () => setIsDropdownOpen(!isDropdownOpen);

    const handleMouseEnter = () => {
        setIsDropdownOpen(true);
    };

    const handleMouseLeave = () => {
        setIsDropdownOpen(false);
    };

    useEffect(() => {
        const handleScroll = () => {
            setIsDropdownOpen(false); // Close dropdown menu on scroll
        };

        window.addEventListener('scroll', handleScroll);

        return () => {
            window.removeEventListener('scroll', handleScroll);
        };
    }, []);


    const scrollToContact = () => {
        const contactElement = document.getElementById('contact');
        if (contactElement) {
            contactElement.scrollIntoView({ behavior: 'smooth' });
        }
        setIsMobileMenuOpen(false); // Close mobile menu
    };

    const scrollToTop = () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
        setIsMobileMenuOpen(false); // Close mobile menu
    };

    const handleNavItemClick = (item: any) => {
        if (item.isDropdown) {
            toggleDropdown();
        }
        else if (item.text === 'Home') {
            if (window.location.pathname === '/') {
                // If already on the homepage, scroll to the top
                scrollToTop();
            } else {
                // Navigate to the homepage
                window.location.href = '/';
            }
        } else if (item.text === 'Kontakt') {
            scrollToContact();
        }
        else {
            setIsMobileMenuOpen(false); // Close mobile menu for other items
        }
    };

    // const handleLogoClick = () => {
        // Mimic clicking on "Hem" for the logo click
        //handleNavItemClick({ text: 'Hem' });
    //};

    const toggleMobileMenu = () => {
        setIsMobileMenuOpen(!isMobileMenuOpen);
    };


    return (
        <div className='bg-white w-full flex justify-center md:sticky top-0 z-10 shadow-md font-bold text-xl text-gray-800'>
            <div className="w-full xl:w-5/6 flex flex-col md:flex-row justify-between items-center">
                <div className="flex justify-between items-center w-full md:w-auto">
                    <h1 className='font-bold opacity-0 md:opacity-100 text-3xl'>
                        Chatbot
                    </h1>
                    <button
                        className="md:hidden p-4"
                        onClick={toggleMobileMenu}
                        aria-label="Toggle mobile menu"
                    >
                        <svg
                            className="w-6 h-6"
                            fill="none"
                            stroke="currentColor"
                            viewBox="0 0 24 24"
                            xmlns="http://www.w3.org/2000/svg"
                        >
                            <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                strokeWidth="2"
                                d={isMobileMenuOpen ? 'M6 18L18 6M6 6l12 12' : 'M4 6h16M4 12h16M4 18h16'}
                            ></path>
                        </svg>
                    </button>
                </div>
                <ul className={`md:flex md:space-x-4 ${isMobileMenuOpen ? 'block' : 'hidden'} md:block w-full md:w-auto md:justify-end ${isMobileMenuOpen ? 'text-right text-2xl' : ''}`}>
                    {navItems.map(item => (
                        <li key={item.id} className={`relative p-4 rounded-xl m-2 duration-300 ${isMobileMenuOpen ? '' : 'hover:scale-110'}`}
                            onMouseEnter={item.isDropdown ? handleMouseEnter : undefined}
                            onMouseLeave={item.isDropdown ? handleMouseLeave : undefined}>
                            <a
                                onClick={(e) => {
                                    e.preventDefault();
                                    handleNavItemClick(item);
                                }}
                                className='cursor-pointer font-semibold hover:text-black'
                                href={item.href}
                                tabIndex={0}
                            >
                                {item.text}
                            </a>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default Navbar;